package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.CSEUser;
import com.chubb.repository.CSEUserRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CSEUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CSEUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cse-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CSEUserRepository cSEUserRepository;

    @Autowired
    private MockMvc restCSEUserMockMvc;

    private CSEUser cSEUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CSEUser createEntity() {
        CSEUser cSEUser = new CSEUser().name(DEFAULT_NAME).email(DEFAULT_EMAIL).phone(DEFAULT_PHONE);
        return cSEUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CSEUser createUpdatedEntity() {
        CSEUser cSEUser = new CSEUser().name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);
        return cSEUser;
    }

    @BeforeEach
    public void initTest() {
        cSEUserRepository.deleteAll();
        cSEUser = createEntity();
    }

    @Test
    void createCSEUser() throws Exception {
        int databaseSizeBeforeCreate = cSEUserRepository.findAll().size();
        // Create the CSEUser
        restCSEUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cSEUser)))
            .andExpect(status().isCreated());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeCreate + 1);
        CSEUser testCSEUser = cSEUserList.get(cSEUserList.size() - 1);
        assertThat(testCSEUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCSEUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCSEUser.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void createCSEUserWithExistingId() throws Exception {
        // Create the CSEUser with an existing ID
        cSEUser.setId(1L);

        int databaseSizeBeforeCreate = cSEUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCSEUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cSEUser)))
            .andExpect(status().isBadRequest());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCSEUsers() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        // Get all the cSEUserList
        restCSEUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cSEUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    void getCSEUser() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        // Get the cSEUser
        restCSEUserMockMvc
            .perform(get(ENTITY_API_URL_ID, cSEUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cSEUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    void getNonExistingCSEUser() throws Exception {
        // Get the cSEUser
        restCSEUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCSEUser() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();

        // Update the cSEUser
        CSEUser updatedCSEUser = cSEUserRepository.findById(cSEUser.getId()).orElseThrow();
        updatedCSEUser.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restCSEUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCSEUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCSEUser))
            )
            .andExpect(status().isOk());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
        CSEUser testCSEUser = cSEUserList.get(cSEUserList.size() - 1);
        assertThat(testCSEUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCSEUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCSEUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void putNonExistingCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cSEUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cSEUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cSEUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cSEUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCSEUserWithPatch() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();

        // Update the cSEUser using partial update
        CSEUser partialUpdatedCSEUser = new CSEUser();
        partialUpdatedCSEUser.setId(cSEUser.getId());

        partialUpdatedCSEUser.name(UPDATED_NAME).email(UPDATED_EMAIL);

        restCSEUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCSEUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCSEUser))
            )
            .andExpect(status().isOk());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
        CSEUser testCSEUser = cSEUserList.get(cSEUserList.size() - 1);
        assertThat(testCSEUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCSEUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCSEUser.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    void fullUpdateCSEUserWithPatch() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();

        // Update the cSEUser using partial update
        CSEUser partialUpdatedCSEUser = new CSEUser();
        partialUpdatedCSEUser.setId(cSEUser.getId());

        partialUpdatedCSEUser.name(UPDATED_NAME).email(UPDATED_EMAIL).phone(UPDATED_PHONE);

        restCSEUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCSEUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCSEUser))
            )
            .andExpect(status().isOk());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
        CSEUser testCSEUser = cSEUserList.get(cSEUserList.size() - 1);
        assertThat(testCSEUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCSEUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCSEUser.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    void patchNonExistingCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cSEUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cSEUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cSEUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCSEUser() throws Exception {
        int databaseSizeBeforeUpdate = cSEUserRepository.findAll().size();
        cSEUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCSEUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cSEUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CSEUser in the database
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCSEUser() throws Exception {
        // Initialize the database
        cSEUserRepository.save(cSEUser);

        int databaseSizeBeforeDelete = cSEUserRepository.findAll().size();

        // Delete the cSEUser
        restCSEUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, cSEUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CSEUser> cSEUserList = cSEUserRepository.findAll();
        assertThat(cSEUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
