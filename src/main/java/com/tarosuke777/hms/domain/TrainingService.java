package com.tarosuke777.hms.domain;

import java.util.List;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tarosuke777.hms.entity.TrainingEntity;
import com.tarosuke777.hms.form.TrainingForm;
import com.tarosuke777.hms.repository.TrainingMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingService {
	private final TrainingMapper trainingMapper;

	public List<TrainingForm> getTrainingList(String orderBy, String sort) {

		return trainingMapper.findMany(orderBy, sort).stream().map(entity -> {
			TrainingForm form = new TrainingForm();
			form.setTrainingId(entity.getTrainingId());
			form.setTrainingDate(entity.getTrainingDate());
			form.setWeight(entity.getWeight());
			form.setReps(entity.getReps());
			form.setSets(entity.getSets());
			form.setTrainingAreaId(entity.getTrainingMenu().getTargetAreaId());
			form.setTrainingMenuId(entity.getTrainingMenu().getTrainingMenuId());
			return form;
		}).toList();
	}

	public TrainingForm getTrainingDetails(Integer trainingId) {
		TrainingEntity training = trainingMapper.findOne(trainingId);
		TrainingForm trainingForm = new TrainingForm();
		trainingForm.setTrainingId(training.getTrainingId());
		trainingForm.setTrainingDate(training.getTrainingDate());
		trainingForm.setWeight(training.getWeight());
		trainingForm.setReps(training.getReps());
		trainingForm.setSets(training.getSets());
		trainingForm.setTrainingMenuId(training.getTrainingMenu().getTrainingMenuId());
		return trainingForm;
	}

	@Transactional
	public void registerTraining(TrainingForm form) {
		trainingMapper.insertOne(form.getTrainingDate(), form.getTrainingMenuId(), form.getWeight(),
				form.getReps(), form.getSets());
	}

	@Transactional
	public void updateTraining(TrainingForm form) {
		trainingMapper.updateOne(form.getTrainingId(), form.getTrainingDate(),
				form.getTrainingMenuId(), form.getWeight(), form.getReps(), form.getSets());
	}

	@Transactional
	public void deleteTraining(Integer trainingId) {
		trainingMapper.deleteOne(trainingId);
	}

	@Tool(description = "日付でトレーニングを取得する", name = "getTraining")
	public String getTrainingForMcp(@ToolParam(description = "日付") String date) {
		var dateWithTraining =
				Map.of("2025-01-01", "チェストプレス", "2025-01-02", "ランニング", "2025-01-03", "水泳");
		return dateWithTraining.getOrDefault(date, "No training scheduled for this date");
	}
}
