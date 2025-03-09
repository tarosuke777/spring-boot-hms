package com.tarosuke777.hms.domain;

import java.util.List;

import org.modelmapper.ModelMapper;
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

	public List<TrainingForm> getTrainingList() {

		return trainingMapper.findMany().stream().map(entity -> {
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
		trainingMapper.insertOne(form.getTrainingDate(), form.getTrainingMenuId(), form.getWeight(), form.getReps(),
				form.getSets());
	}

	@Transactional
	public void updateTraining(TrainingForm form) {
		trainingMapper.updateOne(form.getTrainingId(), form.getTrainingDate(), form.getTrainingMenuId(),
				form.getWeight(), form.getReps(), form.getSets());
	}

	@Transactional
	public void deleteTraining(Integer trainingId) {
		trainingMapper.deleteOne(trainingId);
	}
}
