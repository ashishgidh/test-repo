package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.Branch;
import com.chubb.repository.BranchRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link BranchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BranchResourceIT {

    private static final Long DEFAULT_BRANCH_ID = 1L;
    private static final Long UPDATED_BRANCH_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private MockMvc restBranchMockMvc;

    private Branch branch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createEntity() {
        Branch branch = new Branch().branchId(DEFAULT_BRANCH_ID).name(DEFAULT_NAME);
        return branch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity() {
        Branch branch = new Branch().branchId(UPDATED_BRANCH_ID).name(UPDATED_NAME);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branchRepository.deleteAll();
        branch = createEntity();
    }

    @Test
    void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();
        // Create the Branch
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchId()).isEqualTo(DEFAULT_BRANCH_ID);
        assertThat(testBranch.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createBranchWithExistingId() throws Exception {
        // Create the Branch with an existing ID
        branch.setId("existing_id");

        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        // Get all the branchList
        restBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId())))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getBranch() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        // Get the branch
        restBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId()))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingBranch() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).orElseThrow();
        updatedBranch.branchId(UPDATED_BRANCH_ID).name(UPDATED_NAME);

        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBranch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchId()).isEqualTo(UPDATED_BRANCH_ID);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branch.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(branch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.name(UPDATED_NAME);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchId()).isEqualTo(DEFAULT_BRANCH_ID);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateBranchWithPatch() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch using partial update
        Branch partialUpdatedBranch = new Branch();
        partialUpdatedBranch.setId(branch.getId());

        partialUpdatedBranch.branchId(UPDATED_BRANCH_ID).name(UPDATED_NAME);

        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBranch))
            )
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchId()).isEqualTo(UPDATED_BRANCH_ID);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(branch))
            )
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();
        branch.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(branch)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.save(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, branch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
