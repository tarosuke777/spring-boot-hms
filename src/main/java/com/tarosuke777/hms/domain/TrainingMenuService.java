package com.tarosuke777.hms.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tarosuke777.hms.entity.TrainingMenuEntity;
import com.tarosuke777.hms.form.SelectOption;
import com.tarosuke777.hms.repository.TrainingMenuMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingMenuService {
	private final TrainingMenuMapper trainingMenuMapper;

	@Cacheable("trainingMenuMap")
	public Map<Integer, String> getTrainingMenuMap() {
		return trainingMenuMapper.findMany().stream().collect(Collectors.toMap(TrainingMenuEntity::getTrainingMenuId,
				TrainingMenuEntity::getTrainingMenuName, (existing, replacement) -> existing, LinkedHashMap::new));
	}

	public List<SelectOption> getTrainingMenuSelectList() {
		return trainingMenuMapper.findMany().stream()
				.map(entity -> new SelectOption(entity.getTrainingMenuId().toString(), entity.getTrainingMenuName(), entity.getTargetAreaId().toString()))
				.collect(Collectors.toList());
	}

}
