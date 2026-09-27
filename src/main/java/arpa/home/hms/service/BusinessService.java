package arpa.home.hms.service;

import arpa.home.hms.entity.BusinessEntity;
import arpa.home.hms.entity.BusinessModelCanvasEntity;
import arpa.home.hms.enums.BusinessGenre;
import arpa.home.hms.enums.BusinessStatus;
import arpa.home.hms.form.BusinessForm;
import arpa.home.hms.mapper.BusinessMapper;
import arpa.home.hms.repository.BusinessRepository;
import arpa.home.hms.specification.BusinessSpecifications;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessService {

  private final BusinessRepository businessRepository;
  private final BusinessMapper businessMapper;

  public Page<BusinessForm> getBusinessList(Integer currentUserId, String keyword,
      BusinessGenre genre, BusinessStatus status, @NonNull Pageable pageable) {
    var spec = BusinessSpecifications.withFilters(currentUserId, keyword, genre, status);
    return businessRepository.findAll(spec, pageable).map(businessMapper::toForm);
  }

  public BusinessForm getBusiness(@NonNull Integer businessId, Integer currentUserId) {
    BusinessEntity business = businessRepository.findByIdAndCreatedBy(businessId, currentUserId)
        .orElseThrow(() -> new RuntimeException("Business not found or access denied"));
    return businessMapper.toForm(business);
  }

  @Transactional
  public void registerBusiness(BusinessForm form) {
    BusinessEntity entity = Objects.requireNonNull(businessMapper.toEntity(form));
    entity.setCanvas(createCanvas(entity, form));
    businessRepository.save(entity);
  }

  @Transactional
  public void updateBusiness(BusinessForm form, Integer currentUserId) {
    BusinessEntity existEntity =
        businessRepository.findByIdAndCreatedBy(form.getId(), currentUserId)
            .orElseThrow(() -> new RuntimeException("Business not found or access denied"));

    BusinessEntity entity = Objects.requireNonNull(businessMapper.copy(existEntity));
    businessMapper.updateEntityFromForm(form, entity);

    BusinessModelCanvasEntity canvas =
        entity.getCanvas() != null ? entity.getCanvas() : new BusinessModelCanvasEntity();
    canvas.setBusiness(entity);
    applyCanvasValues(canvas, form);
    entity.setCanvas(canvas);

    businessRepository.save(entity);
  }

  @Transactional
  public void deleteBusiness(@NonNull Integer businessId, Integer currentUserId) {
    if (!businessRepository.existsByIdAndCreatedBy(businessId, currentUserId)) {
      throw new RuntimeException("Business not found or access denied");
    }
    businessRepository.deleteById(businessId);
  }

  private BusinessModelCanvasEntity createCanvas(BusinessEntity entity, BusinessForm form) {
    BusinessModelCanvasEntity canvas = new BusinessModelCanvasEntity();
    canvas.setBusiness(entity);
    applyCanvasValues(canvas, form);
    return canvas;
  }

  private void applyCanvasValues(BusinessModelCanvasEntity canvas, BusinessForm form) {
    canvas.setCustomerSegments(form.getCustomerSegments());
    canvas.setValueProposition(form.getValueProposition());
    canvas.setChannels(form.getChannels());
    canvas.setCustomerRelationships(form.getCustomerRelationships());
    canvas.setRevenueStreams(form.getRevenueStreams());
    canvas.setKeyResources(form.getKeyResources());
    canvas.setKeyActivities(form.getKeyActivities());
    canvas.setKeyPartners(form.getKeyPartners());
    canvas.setCostStructure(form.getCostStructure());
  }
}
