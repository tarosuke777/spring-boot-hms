package com.tarosuke777.hms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.SelectOptionTrainingMenu;
import com.tarosuke777.hms.form.TrainingMenuForm;
import com.tarosuke777.hms.mapper.TrainingMenuMapper;
import com.tarosuke777.hms.repository.TrainingMenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingMenuService {

	private final TrainingMenuRepository trainingMenuRepository;
	private final TrainingMenuMapper trainingMenuMapper;

	public List<TrainingMenuForm> getTrainingMenuList(Integer currentUserId) {
		return trainingMenuRepository.findByCreatedBy(currentUserId).stream()
				.map(trainingMenuMapper::toForm).toList();
	}

	public TrainingMenuForm getTrainingMenuDetails(Integer id, Integer currentUserId) {
		TrainingMenuEntity entity = trainingMenuRepository.findByIdAndCreatedBy(id, currentUserId)
				.orElseThrow(() -> new RuntimeException("Menu not found or access denied"));
		return trainingMenuMapper.toForm(entity);
	}

	@Transactional
	public void registerTrainingMenu(TrainingMenuForm form) {
		TrainingMenuEntity entity = trainingMenuMapper.toEntity(form);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void updateTrainingMenu(TrainingMenuForm form, Integer currentUserId) {
		TrainingMenuEntity existEntity =
				trainingMenuRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
						.orElseThrow(() -> new RuntimeException("Menu not found or access denied"));
		TrainingMenuEntity entity = trainingMenuMapper.copy(existEntity);
		trainingMenuMapper.updateEntityFromForm(form, entity);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void deleteTrainingMenu(Integer id, Integer currentUserId) {
		if (!trainingMenuRepository.existsByIdAndCreatedBy(id, currentUserId)) {
			throw new RuntimeException("Menu not found or access denied");
		}
		trainingMenuRepository.deleteById(id);
	}

	public Map<Integer, String> getTrainingMenuMap() {
		return trainingMenuRepository.findAll().stream()
				.collect(Collectors.toMap(TrainingMenuEntity::getId, TrainingMenuEntity::getName,
						(existing, replacement) -> existing, LinkedHashMap::new));
	}

	public List<SelectOptionTrainingMenu> getTrainingMenuSelectList() {
		return trainingMenuRepository.findAll().stream()
				.map(entity -> new SelectOptionTrainingMenu(entity.getId().toString(),
						entity.getName(), entity.getTargetAreaId(), entity.getMaxWeight(),
						entity.getMaxReps(), entity.getMaxSets()))
				.collect(Collectors.toList());
	}

}
