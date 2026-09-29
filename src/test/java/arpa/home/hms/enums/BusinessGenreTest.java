package arpa.home.hms.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BusinessGenreTest {

  @Test
  void fromCode_ShouldReturnMatchingGenre() {
    assertEquals(BusinessGenre.SYSTEM_DEV, BusinessGenre.fromCode(1));
    assertEquals(BusinessGenre.MOBILE_APP, BusinessGenre.fromCode(2));
    assertEquals(BusinessGenre.TECH_MEDIA, BusinessGenre.fromCode(3));
    assertEquals(BusinessGenre.DEV_TOOL, BusinessGenre.fromCode(4));
    assertEquals(BusinessGenre.SAAS_SERVICE, BusinessGenre.fromCode(5));
    assertEquals(BusinessGenre.IT_CONSULTING, BusinessGenre.fromCode(6));
    assertEquals(BusinessGenre.OTHER, BusinessGenre.fromCode(99));
  }

  @Test
  void fromCode_WithUnknownCode_ShouldThrow() {
    assertThrows(IllegalArgumentException.class, () -> BusinessGenre.fromCode(0));
    assertThrows(IllegalArgumentException.class, () -> BusinessGenre.fromCode(-1));
  }

  @Test
  void safeValueOf_ShouldReturnMatchingGenreOrOther() {
    assertEquals(BusinessGenre.SAAS_SERVICE, BusinessGenre.safeValueOf("SAAS_SERVICE"));
    assertEquals(BusinessGenre.OTHER, BusinessGenre.safeValueOf("UNKNOWN"));
    assertEquals(BusinessGenre.OTHER, BusinessGenre.safeValueOf(null));
  }

  @Test
  void genres_ShouldExposeLabelsAndBadgeClasses() {
    assertEquals("受託・開発支援", BusinessGenre.SYSTEM_DEV.getLabel());
    assertEquals("bg-primary", BusinessGenre.SYSTEM_DEV.getBadgeClass());
    assertEquals("その他", BusinessGenre.OTHER.getLabel());
    assertEquals("bg-light text-dark", BusinessGenre.OTHER.getBadgeClass());
  }
}
