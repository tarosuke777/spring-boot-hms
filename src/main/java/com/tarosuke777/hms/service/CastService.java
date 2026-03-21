package com.tarosuke777.hms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.CastEntity;
import com.tarosuke777.hms.form.CastForm;
import com.tarosuke777.hms.mapper.CastMapper;
import com.tarosuke777.hms.repository.CastRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CastService {

    private final CastRepository castRepository;
    private final CastMapper castMapper;

    public List<CastForm> getCastList(Integer currentUserId) {
        return castRepository.findByCreatedBy(currentUserId).stream().map(castMapper::toForm)
                .toList();
    }

    public CastForm getCast(Integer castId, Integer currentUserId) {
        CastEntity cast = castRepository.findByIdAndCreatedBy(castId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Cast not found or access denied"));
        return castMapper.toForm(cast);
    }

    @Transactional
    public void registerCast(CastForm form) {
        CastEntity entity = castMapper.toEntity(form);
        castRepository.save(entity);
    }

    @Transactional
    public void updateCast(CastForm form, Integer currentUserId) {
        CastEntity existEntity = castRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
                .orElseThrow(() -> new RuntimeException("Cast not found or access denied"));
        CastEntity entity = castMapper.copy(existEntity);
        castMapper.updateEntityFromForm(form, entity);
        castRepository.save(entity);
    }

    @Transactional
    public void deleteCast(Integer castId, Integer currentUserId) {
        if (!castRepository.existsByIdAndCreatedBy(castId, currentUserId)) {
            throw new RuntimeException("Cast not found or access denied");
        }
        castRepository.deleteById(castId);
    }

    public Map<Integer, String> getCastMap() {
        return castRepository.findAll().stream().collect(Collectors.toMap(CastEntity::getId,
                CastEntity::getName, (existing, replacement) -> existing, LinkedHashMap::new));
    }
}
