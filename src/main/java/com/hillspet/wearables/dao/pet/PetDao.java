package com.hillspet.wearables.dao.pet;

import java.util.List;
import java.util.Map;

import com.hillspet.wearables.common.exceptions.ServiceExecutionException;
import com.hillspet.wearables.dto.BehaviorHistory;
import com.hillspet.wearables.dto.EatingEnthusiasmScale;
import com.hillspet.wearables.dto.ImageScoringScale;
import com.hillspet.wearables.dto.PetBehavior;
import com.hillspet.wearables.dto.PetBreed;
import com.hillspet.wearables.dto.PetDTO;
import com.hillspet.wearables.dto.PetFeedingEnthusiasmScale;
import com.hillspet.wearables.dto.PetFeedingPreference;
import com.hillspet.wearables.dto.PetFeedingTime;
import com.hillspet.wearables.dto.PetMobileAppConfig;
import com.hillspet.wearables.dto.PetObservation;
import com.hillspet.wearables.dto.PetSpecies;
import com.hillspet.wearables.dto.PetWeightDTO;
import com.hillspet.wearables.request.AddPetWeight;
import com.hillspet.wearables.request.BehaviorHistoryRequest;
import com.hillspet.wearables.request.AssignSensorRequest;
import com.hillspet.wearables.request.FMGoalSettingRequest;
import com.hillspet.wearables.request.PetAddFeedingPreferences;
import com.hillspet.wearables.request.PetAddImageScoring;
import com.hillspet.wearables.request.PetIds;
import com.hillspet.wearables.request.UpdatePet;
import com.hillspet.wearables.request.UpdatePetWeight;
import com.hillspet.wearables.response.BehaviorVisualizationResponse;
import com.hillspet.wearables.response.PetAddressResponse;
import com.hillspet.wearables.response.PetWeightHistoryResponse;

public interface PetDao {

	PetWeightDTO addPetWeight(AddPetWeight addPetWeight) throws ServiceExecutionException;

	PetWeightDTO updateWeight(UpdatePetWeight updatePetWeight) throws ServiceExecutionException;

	void updatePetPhoto(int petId, int petParentId, String petPhoto) throws ServiceExecutionException;

	PetWeightHistoryResponse getPetWeightHistory(int petId, String fromDate, String toDate)
			throws ServiceExecutionException;

	List<PetSpecies> getPetSpecies() throws ServiceExecutionException;

	List<PetBreed> getPetBreeds(int speciesId) throws ServiceExecutionException;

	List<PetBehavior> getPetBehaviors(int speciesId, int behaviorTypeId) throws ServiceExecutionException;

	List<EatingEnthusiasmScale> getPetEatingEnthusiasmScale(int speciesId) throws ServiceExecutionException;

	List<PetFeedingTime> getPetFeedingTime() throws ServiceExecutionException;

	Map<Integer, List<PetMobileAppConfig>> getMobileAppConfigs(PetIds petIds) throws ServiceExecutionException;

	PetFeedingEnthusiasmScale addPetFeedingTime(PetFeedingEnthusiasmScale petFeedingEnthusiasmScale)
			throws ServiceExecutionException;

	List<PetObservation> getPetObservationsByPetId(int petId) throws ServiceExecutionException;

	PetObservation savePetObservation(PetObservation addPetObservation) throws ServiceExecutionException;

	void deletePetObservation(int observationId, int petId, int petParentId) throws ServiceExecutionException;

	List<ImageScoringScale> getPetImageScoringScales(int petId) throws ServiceExecutionException;

	PetAddImageScoring addPetImageScoring(PetAddImageScoring addPetImageScorings) throws ServiceExecutionException;

	List<PetFeedingPreference> getPetFeedingPreferences() throws ServiceExecutionException;

	void addPetFeedingPreferences(PetAddFeedingPreferences petAddFeedingPreferences) throws ServiceExecutionException;

	void updatePet(UpdatePet updatePet) throws ServiceExecutionException;

	PetAddressResponse getPetAddressHistoryById(int petId) throws ServiceExecutionException;

	List<PetDTO> getPetsByPetParentIdAndMobileAppConfigId(int petParentId, int mobileAppConfigId) throws ServiceExecutionException;
	
	BehaviorVisualizationResponse getBehaviorVisualization(int petId) throws ServiceExecutionException;

	List<BehaviorHistory> getBehaviorHistoryByType(int petId, String behaviorType, BehaviorHistoryRequest behaviorHistoryRequest) throws ServiceExecutionException;

	void replaceSensorToPet(AssignSensorRequest assignSensorRequest) throws ServiceExecutionException;

	void saveForwardMotionGoal(FMGoalSettingRequest fmGoalSettingRequest);
}
