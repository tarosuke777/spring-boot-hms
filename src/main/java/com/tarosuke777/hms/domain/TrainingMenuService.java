package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.SelectOptionTrainingMenu;
import com.tarosuke777.hms.form.TrainingMenuForm;
import com.tarosuke777.hms.repository.TrainingMenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingMenuService {

	private final TrainingMenuRepository trainingMenuRepository;
	private final ModelMapper modelMapper;

	public List<TrainingMenuForm> getTrainingMenuList() {
		return trainingMenuRepository.findAll().stream()
				.map(entity -> modelMapper.map(entity, TrainingMenuForm.class)).toList();
	}

	public TrainingMenuForm getTrainingMenuDetails(Integer trainingMenuId) {
		TrainingMenuEntity entity = trainingMenuRepository.findById(trainingMenuId)
				.orElseThrow(() -> new RuntimeException("Menu not found"));
		return modelMapper.map(entity, TrainingMenuForm.class);
	}

	@Transactional
	public void registerTrainingMenu(TrainingMenuForm form) {
		TrainingMenuEntity entity = modelMapper.map(form, TrainingMenuEntity.class);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void updateTrainingMenu(TrainingMenuForm form) {
		TrainingMenuEntity existEntity = trainingMenuRepository.findById(form.getTrainingMenuId())
				.orElseThrow(() -> new RuntimeException("Menu not found"));
		TrainingMenuEntity entity = new TrainingMenuEntity();
		modelMapper.map(existEntity, entity);
		modelMapper.map(form, entity);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void deleteTrainingMenu(Integer trainingMenuId) {
		trainingMenuRepository.deleteById(trainingMenuId);
	}

	public Map<Integer, String> getTrainingMenuMap() {
		return trainingMenuRepository.findAll().stream()
				.collect(Collectors.toMap(TrainingMenuEntity::getTrainingMenuId,
						TrainingMenuEntity::getTrainingMenuName,
						(existing, replacement) -> existing, LinkedHashMap::new));
	}

	public List<SelectOptionTrainingMenu> getTrainingMenuSelectList() {
		return trainingMenuRepository.findAll().stream()
				.map(entity -> new SelectOptionTrainingMenu(entity.getTrainingMenuId().toString(),
						entity.getTrainingMenuName(), entity.getTargetAreaId(),
						entity.getMaxWeight(), entity.getMaxReps(), entity.getMaxSets()))
				.collect(Collectors.toList());
	}

}
