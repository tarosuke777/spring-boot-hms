package com.tarosuke777.hms.domain;

import java.util.List;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.TrainingEntity;
import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.TrainingForm;
import com.tarosuke777.hms.repository.TrainingRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {

	private final TrainingRepository trainingRepository;
	private final EntityManager entityManager;
	private final ModelMapper modelMapper;

	public List<TrainingForm> getTrainingList(String orderBy, String sortDirection,
			String currentUserId) {
		Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(orderBy).descending()
				: Sort.by(orderBy).ascending();
		return trainingRepository.findByCreatedBy(currentUserId, sort).stream()
				.map(this::convertToForm).toList();
	}

	public TrainingForm getTrainingDetails(Integer trainingId, String currentUserId) {
		return trainingRepository.findByTrainingIdAndCreatedBy(trainingId, currentUserId)
				.map(this::convertToForm)
				.orElseThrow(() -> new RuntimeException("Training not found or access denied"));
	}

	@Transactional
	public void registerTraining(TrainingForm form) {
		TrainingEntity entity = modelMapper.map(form, TrainingEntity.class);
		if (form.getTrainingMenuId() != null) {
			entity.setTrainingMenu(
					entityManager.getReference(TrainingMenuEntity.class, form.getTrainingMenuId()));
		}
		trainingRepository.save(entity);
	}

	@Transactional
	public void updateTraining(TrainingForm form, String currentUserId) {
		TrainingEntity existingEntity = trainingRepository
				.findByTrainingIdAndCreatedBy(form.getTrainingId(), currentUserId)
				.orElseThrow(() -> new RuntimeException("Training not found or access denied"));

		TrainingEntity entity = new TrainingEntity();
		modelMapper.map(existingEntity, entity);
		if (existingEntity.getTrainingMenu() != null) {
			entity.setTrainingMenu(entityManager.getReference(TrainingMenuEntity.class,
					existingEntity.getTrainingMenu().getTrainingMenuId()));
		}

		modelMapper.map(form, entity);
		if (form.getTrainingMenuId() != null) {
			entity.setTrainingMenu(
					entityManager.getReference(TrainingMenuEntity.class, form.getTrainingMenuId()));
		}
		trainingRepository.save(entity);
	}

	@Transactional
	public void deleteTraining(Integer trainingId, String currentUserId) {
		if (!trainingRepository.existsByTrainingIdAndCreatedBy(trainingId, currentUserId)) {
			throw new RuntimeException("Training not found or access denied");
		}
		trainingRepository.deleteById(trainingId);
	}


	/** Entity から Form への変換（リレーションのID詰め替え含む） */
	private TrainingForm convertToForm(TrainingEntity entity) {
		TrainingForm form = modelMapper.map(entity, TrainingForm.class);
		if (entity.getTrainingMenu() != null) {
			form.setTrainingMenuId(entity.getTrainingMenu().getTrainingMenuId());
			form.setTrainingAreaId(entity.getTrainingMenu().getTargetAreaId());
		}
		return form;
	}

	@Tool(description = "日付でトレーニングを取得する", name = "getTraining")
	public String getTrainingForMcp(@ToolParam(description = "日付") String date) {
		var dateWithTraining =
				Map.of("2025-01-01", "チェストプレス", "2025-01-02", "ランニング", "2025-01-03", "水泳");
		return dateWithTraining.getOrDefault(date, "No training scheduled for this date");
	}
}
