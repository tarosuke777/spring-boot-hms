package com.tarosuke777.hms.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tarosuke777.hms.entity.CompanyEntity;
import com.tarosuke777.hms.form.CompanyForm;
import com.tarosuke777.hms.mapper.CompanyMapper;
import com.tarosuke777.hms.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public List<CompanyForm> getCompanyList(Integer currentUserId) {
        return companyRepository.findByCreatedBy(currentUserId).stream().map(companyMapper::toForm)
                .toList();
    }

    public CompanyForm getCompany(Integer companyId, Integer currentUserId) {
        CompanyEntity company = companyRepository.findByIdAndCreatedBy(companyId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Company not found or access denied"));
        return companyMapper.toForm(company);
    }

    @Transactional
    public void registerCompany(CompanyForm form) {
        CompanyEntity entity = companyMapper.toEntity(form);
        companyRepository.save(entity);
    }

    @Transactional
    public void updateCompany(CompanyForm form, Integer currentUserId) {
        CompanyEntity existEntity =
                companyRepository.findByIdAndCreatedBy(form.getId(), currentUserId).orElseThrow(
                        () -> new RuntimeException("Company not found or access denied"));
        CompanyEntity entity = companyMapper.copy(existEntity);
        companyMapper.updateEntityFromForm(form, entity);
        companyRepository.save(entity);
    }

    @Transactional
    public void deleteCompany(Integer companyId, Integer currentUserId) {
        if (!companyRepository.existsByIdAndCreatedBy(companyId, currentUserId)) {
            throw new RuntimeException("Company not found or access denied");
        }
        companyRepository.deleteById(companyId);
    }

    public Map<Integer, String> getCompanyMap() {
        return companyRepository.findAll().stream().collect(Collectors.toMap(CompanyEntity::getId,
                CompanyEntity::getName, (existing, replacement) -> existing, LinkedHashMap::new));
    }
}
