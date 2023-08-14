package com.chubb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.chubb.IntegrationTest;
import com.chubb.domain.Program;
import com.chubb.repository.ProgramRepository;
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
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramResourceIT {

    private static final Long DEFAULT_PROGRAM_ID = 1L;
    private static final Long UPDATED_PROGRAM_ID = 2L;

    private static final Long DEFAULT_CLUSTER_ID = 1L;
    private static final Long UPDATED_CLUSTER_ID = 2L;

    private static final Long DEFAULT_COUNTRY_ID = 1L;
    private static final Long UPDATED_COUNTRY_ID = 2L;

    private static final Long DEFAULT_BRANCH_ID = 1L;
    private static final Long UPDATED_BRANCH_ID = 2L;

    private static final Long DEFAULT_MAX_LOB_ID = 1L;
    private static final Long UPDATED_MAX_LOB_ID = 2L;

    private static final Long DEFAULT_WV_LOB_ID = 1L;
    private static final Long UPDATED_WV_LOB_ID = 2L;

    private static final String DEFAULT_PROGRAM_EFFECTIVE_DATE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_EFFECTIVE_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_PROGRAM_EXPIRY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_PROGRAM_EXPIRY_DATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private MockMvc restProgramMockMvc;

    private Program program;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createEntity() {
        Program program = new Program()
            .programId(DEFAULT_PROGRAM_ID)
            .clusterId(DEFAULT_CLUSTER_ID)
            .countryId(DEFAULT_COUNTRY_ID)
            .branchId(DEFAULT_BRANCH_ID)
            .maxLOBId(DEFAULT_MAX_LOB_ID)
            .wvLOBId(DEFAULT_WV_LOB_ID)
            .programEffectiveDate(DEFAULT_PROGRAM_EFFECTIVE_DATE)
            .programExpiryDate(DEFAULT_PROGRAM_EXPIRY_DATE);
        return program;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity() {
        Program program = new Program()
            .programId(UPDATED_PROGRAM_ID)
            .clusterId(UPDATED_CLUSTER_ID)
            .countryId(UPDATED_COUNTRY_ID)
            .branchId(UPDATED_BRANCH_ID)
            .maxLOBId(UPDATED_MAX_LOB_ID)
            .wvLOBId(UPDATED_WV_LOB_ID)
            .programEffectiveDate(UPDATED_PROGRAM_EFFECTIVE_DATE)
            .programExpiryDate(UPDATED_PROGRAM_EXPIRY_DATE);
        return program;
    }

    @BeforeEach
    public void initTest() {
        programRepository.deleteAll();
        program = createEntity();
    }

    @Test
    void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();
        // Create the Program
        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
        assertThat(testProgram.getClusterId()).isEqualTo(DEFAULT_CLUSTER_ID);
        assertThat(testProgram.getCountryId()).isEqualTo(DEFAULT_COUNTRY_ID);
        assertThat(testProgram.getBranchId()).isEqualTo(DEFAULT_BRANCH_ID);
        assertThat(testProgram.getMaxLOBId()).isEqualTo(DEFAULT_MAX_LOB_ID);
        assertThat(testProgram.getWvLOBId()).isEqualTo(DEFAULT_WV_LOB_ID);
        assertThat(testProgram.getProgramEffectiveDate()).isEqualTo(DEFAULT_PROGRAM_EFFECTIVE_DATE);
        assertThat(testProgram.getProgramExpiryDate()).isEqualTo(DEFAULT_PROGRAM_EXPIRY_DATE);
    }

    @Test
    void createProgramWithExistingId() throws Exception {
        // Create the Program with an existing ID
        program.setId("existing_id");

        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.save(program);

        // Get all the programList
        restProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId())))
            .andExpect(jsonPath("$.[*].programId").value(hasItem(DEFAULT_PROGRAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].clusterId").value(hasItem(DEFAULT_CLUSTER_ID.intValue())))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(DEFAULT_COUNTRY_ID.intValue())))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].maxLOBId").value(hasItem(DEFAULT_MAX_LOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].wvLOBId").value(hasItem(DEFAULT_WV_LOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].programEffectiveDate").value(hasItem(DEFAULT_PROGRAM_EFFECTIVE_DATE)))
            .andExpect(jsonPath("$.[*].programExpiryDate").value(hasItem(DEFAULT_PROGRAM_EXPIRY_DATE)));
    }

    @Test
    void getProgram() throws Exception {
        // Initialize the database
        programRepository.save(program);

        // Get the program
        restProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId()))
            .andExpect(jsonPath("$.programId").value(DEFAULT_PROGRAM_ID.intValue()))
            .andExpect(jsonPath("$.clusterId").value(DEFAULT_CLUSTER_ID.intValue()))
            .andExpect(jsonPath("$.countryId").value(DEFAULT_COUNTRY_ID.intValue()))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID.intValue()))
            .andExpect(jsonPath("$.maxLOBId").value(DEFAULT_MAX_LOB_ID.intValue()))
            .andExpect(jsonPath("$.wvLOBId").value(DEFAULT_WV_LOB_ID.intValue()))
            .andExpect(jsonPath("$.programEffectiveDate").value(DEFAULT_PROGRAM_EFFECTIVE_DATE))
            .andExpect(jsonPath("$.programExpiryDate").value(DEFAULT_PROGRAM_EXPIRY_DATE));
    }

    @Test
    void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingProgram() throws Exception {
        // Initialize the database
        programRepository.save(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).orElseThrow();
        updatedProgram
            .programId(UPDATED_PROGRAM_ID)
            .clusterId(UPDATED_CLUSTER_ID)
            .countryId(UPDATED_COUNTRY_ID)
            .branchId(UPDATED_BRANCH_ID)
            .maxLOBId(UPDATED_MAX_LOB_ID)
            .wvLOBId(UPDATED_WV_LOB_ID)
            .programEffectiveDate(UPDATED_PROGRAM_EFFECTIVE_DATE)
            .programExpiryDate(UPDATED_PROGRAM_EXPIRY_DATE);

        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testProgram.getClusterId()).isEqualTo(UPDATED_CLUSTER_ID);
        assertThat(testProgram.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testProgram.getBranchId()).isEqualTo(UPDATED_BRANCH_ID);
        assertThat(testProgram.getMaxLOBId()).isEqualTo(UPDATED_MAX_LOB_ID);
        assertThat(testProgram.getWvLOBId()).isEqualTo(UPDATED_WV_LOB_ID);
        assertThat(testProgram.getProgramEffectiveDate()).isEqualTo(UPDATED_PROGRAM_EFFECTIVE_DATE);
        assertThat(testProgram.getProgramExpiryDate()).isEqualTo(UPDATED_PROGRAM_EXPIRY_DATE);
    }

    @Test
    void putNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, program.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        programRepository.save(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram
            .programId(UPDATED_PROGRAM_ID)
            .maxLOBId(UPDATED_MAX_LOB_ID)
            .wvLOBId(UPDATED_WV_LOB_ID)
            .programEffectiveDate(UPDATED_PROGRAM_EFFECTIVE_DATE);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testProgram.getClusterId()).isEqualTo(DEFAULT_CLUSTER_ID);
        assertThat(testProgram.getCountryId()).isEqualTo(DEFAULT_COUNTRY_ID);
        assertThat(testProgram.getBranchId()).isEqualTo(DEFAULT_BRANCH_ID);
        assertThat(testProgram.getMaxLOBId()).isEqualTo(UPDATED_MAX_LOB_ID);
        assertThat(testProgram.getWvLOBId()).isEqualTo(UPDATED_WV_LOB_ID);
        assertThat(testProgram.getProgramEffectiveDate()).isEqualTo(UPDATED_PROGRAM_EFFECTIVE_DATE);
        assertThat(testProgram.getProgramExpiryDate()).isEqualTo(DEFAULT_PROGRAM_EXPIRY_DATE);
    }

    @Test
    void fullUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        programRepository.save(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram
            .programId(UPDATED_PROGRAM_ID)
            .clusterId(UPDATED_CLUSTER_ID)
            .countryId(UPDATED_COUNTRY_ID)
            .branchId(UPDATED_BRANCH_ID)
            .maxLOBId(UPDATED_MAX_LOB_ID)
            .wvLOBId(UPDATED_WV_LOB_ID)
            .programEffectiveDate(UPDATED_PROGRAM_EFFECTIVE_DATE)
            .programExpiryDate(UPDATED_PROGRAM_EXPIRY_DATE);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramId()).isEqualTo(UPDATED_PROGRAM_ID);
        assertThat(testProgram.getClusterId()).isEqualTo(UPDATED_CLUSTER_ID);
        assertThat(testProgram.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testProgram.getBranchId()).isEqualTo(UPDATED_BRANCH_ID);
        assertThat(testProgram.getMaxLOBId()).isEqualTo(UPDATED_MAX_LOB_ID);
        assertThat(testProgram.getWvLOBId()).isEqualTo(UPDATED_WV_LOB_ID);
        assertThat(testProgram.getProgramEffectiveDate()).isEqualTo(UPDATED_PROGRAM_EFFECTIVE_DATE);
        assertThat(testProgram.getProgramExpiryDate()).isEqualTo(UPDATED_PROGRAM_EXPIRY_DATE);
    }

    @Test
    void patchNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, program.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(program))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();
        program.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProgram() throws Exception {
        // Initialize the database
        programRepository.save(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, program.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
