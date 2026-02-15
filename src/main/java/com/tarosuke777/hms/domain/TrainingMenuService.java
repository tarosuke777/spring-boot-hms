package com.tarosuke777.hms.domain;

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

	public List<TrainingMenuForm> getTrainingMenuList(String currentUserId) {
		return trainingMenuRepository.findByCreatedBy(currentUserId).stream()
				.map(trainingMenuMapper::toForm).toList();
	}

	public TrainingMenuForm getTrainingMenuDetails(Integer trainingMenuId, String currentUserId) {
		TrainingMenuEntity entity = trainingMenuRepository
				.findByTrainingMenuIdAndCreatedBy(trainingMenuId, currentUserId)
				.orElseThrow(() -> new RuntimeException("Menu not found or access denied"));
		return trainingMenuMapper.toForm(entity);
	}

	@Transactional
	public void registerTrainingMenu(TrainingMenuForm form) {
		TrainingMenuEntity entity = trainingMenuMapper.toEntity(form);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void updateTrainingMenu(TrainingMenuForm form, String currentUserId) {
		TrainingMenuEntity existEntity = trainingMenuRepository
				.findByTrainingMenuIdAndCreatedBy(form.getTrainingMenuId(), currentUserId)
				.orElseThrow(() -> new RuntimeException("Menu not found or access denied"));
		TrainingMenuEntity entity = trainingMenuMapper.copy(existEntity);
		trainingMenuMapper.updateEntityFromForm(form, entity);
		trainingMenuRepository.save(entity);
	}

	@Transactional
	public void deleteTrainingMenu(Integer trainingMenuId, String currentUserId) {
		if (!trainingMenuRepository.existsByTrainingMenuIdAndCreatedBy(trainingMenuId,
				currentUserId)) {
			throw new RuntimeException("Menu not found or access denied");
		}
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
