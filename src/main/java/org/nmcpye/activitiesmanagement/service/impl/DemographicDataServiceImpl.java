package org.nmcpye.activitiesmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.nmcpye.activitiesmanagement.domain.DemographicData;
import org.nmcpye.activitiesmanagement.repository.DemographicDataRepository;
import org.nmcpye.activitiesmanagement.service.DemographicDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DemographicData}.
 */
@Service
@Transactional
public class DemographicDataServiceImpl implements DemographicDataService {

    private final Logger log = LoggerFactory.getLogger(DemographicDataServiceImpl.class);

    private final DemographicDataRepository demographicDataRepository;

    public DemographicDataServiceImpl(DemographicDataRepository demographicDataRepository) {
        this.demographicDataRepository = demographicDataRepository;
    }

    @Override
    public DemographicData save(DemographicData demographicData) {
        log.debug("Request to save DemographicData : {}", demographicData);
        return demographicDataRepository.save(demographicData);
    }

    @Override
    public Optional<DemographicData> partialUpdate(DemographicData demographicData) {
        log.debug("Request to partially update DemographicData : {}", demographicData);

        return demographicDataRepository
            .findById(demographicData.getId())
            .map(
                existingDemographicData -> {
                    if (demographicData.getCreated() != null) {
                        existingDemographicData.setCreated(demographicData.getCreated());
                    }
                    if (demographicData.getLastUpdated() != null) {
                        existingDemographicData.setLastUpdated(demographicData.getLastUpdated());
                    }
                    if (demographicData.getDate() != null) {
                        existingDemographicData.setDate(demographicData.getDate());
                    }
                    if (demographicData.getLevel() != null) {
                        existingDemographicData.setLevel(demographicData.getLevel());
                    }
                    if (demographicData.getTotalPopulation() != null) {
                        existingDemographicData.setTotalPopulation(demographicData.getTotalPopulation());
                    }
                    if (demographicData.getMalePopulation() != null) {
                        existingDemographicData.setMalePopulation(demographicData.getMalePopulation());
                    }
                    if (demographicData.getFemalePopulation() != null) {
                        existingDemographicData.setFemalePopulation(demographicData.getFemalePopulation());
                    }
                    if (demographicData.getLessThan5Population() != null) {
                        existingDemographicData.setLessThan5Population(demographicData.getLessThan5Population());
                    }
                    if (demographicData.getGreaterThan5Population() != null) {
                        existingDemographicData.setGreaterThan5Population(demographicData.getGreaterThan5Population());
                    }
                    if (demographicData.getBw5And15Population() != null) {
                        existingDemographicData.setBw5And15Population(demographicData.getBw5And15Population());
                    }
                    if (demographicData.getGreaterThan15Population() != null) {
                        existingDemographicData.setGreaterThan15Population(demographicData.getGreaterThan15Population());
                    }
                    if (demographicData.getHousehold() != null) {
                        existingDemographicData.setHousehold(demographicData.getHousehold());
                    }
                    if (demographicData.getHouses() != null) {
                        existingDemographicData.setHouses(demographicData.getHouses());
                    }
                    if (demographicData.getHealthFacilities() != null) {
                        existingDemographicData.setHealthFacilities(demographicData.getHealthFacilities());
                    }
                    if (demographicData.getAvgNoOfRooms() != null) {
                        existingDemographicData.setAvgNoOfRooms(demographicData.getAvgNoOfRooms());
                    }
                    if (demographicData.getAvgRoomArea() != null) {
                        existingDemographicData.setAvgRoomArea(demographicData.getAvgRoomArea());
                    }
                    if (demographicData.getAvgHouseArea() != null) {
                        existingDemographicData.setAvgHouseArea(demographicData.getAvgHouseArea());
                    }
                    if (demographicData.getIndividualsPerHousehold() != null) {
                        existingDemographicData.setIndividualsPerHousehold(demographicData.getIndividualsPerHousehold());
                    }
                    if (demographicData.getPopulationGrowthRate() != null) {
                        existingDemographicData.setPopulationGrowthRate(demographicData.getPopulationGrowthRate());
                    }
                    if (demographicData.getComment() != null) {
                        existingDemographicData.setComment(demographicData.getComment());
                    }

                    return existingDemographicData;
                }
            )
            .map(demographicDataRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DemographicData> findAll() {
        log.debug("Request to get all DemographicData");
        return demographicDataRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemographicData> findOne(Long id) {
        log.debug("Request to get DemographicData : {}", id);
        return demographicDataRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DemographicData : {}", id);
        demographicDataRepository.deleteById(id);
    }
}
