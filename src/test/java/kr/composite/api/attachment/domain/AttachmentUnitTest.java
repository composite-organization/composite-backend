package kr.composite.api.attachment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AttachmentUnitTest {

    @Nested
    @DisplayName("적절한 단위 추출 검증")
    class AppropriateUnitValidation {

        @Test
        void 입력값이_1MB_이상이면_MB_단위를_반환한다() {
            // given
            long oneMegaByte = 1024L * 1024L;
            long heavySize = oneMegaByte + 100L;

            // when
            AttachmentUnit unit1 = AttachmentUnit.getAppropriateUnit(oneMegaByte);
            AttachmentUnit unit2 = AttachmentUnit.getAppropriateUnit(heavySize);

            // then
            assertAll(
                    () -> assertThat(unit1).isEqualTo(AttachmentUnit.MB),
                    () -> assertThat(unit2).isEqualTo(AttachmentUnit.MB)
            );
        }

        @Test
        void 입력값이_1MB_미만이면_KB_단위를_반환한다() {
            // given
            long justUnderOneMegaByte = (1024L * 1024L) - 1L;
            long smallSize = 512L;

            // when
            AttachmentUnit unit1 = AttachmentUnit.getAppropriateUnit(justUnderOneMegaByte);
            AttachmentUnit unit2 = AttachmentUnit.getAppropriateUnit(smallSize);

            // then
            assertAll(
                    () -> assertThat(unit1).isEqualTo(AttachmentUnit.KB),
                    () -> assertThat(unit2).isEqualTo(AttachmentUnit.KB)
            );
        }
    }

    @Nested
    @DisplayName("설명 기반 단위 조회 검증")
    class DescriptionMappingValidation {

        @Test
        void 유효한_설명이_주어지면_대응하는_Enum_상수를_반환한다() {
            // when
            AttachmentUnit mb = AttachmentUnit.fromDescription("MB");
            AttachmentUnit kb = AttachmentUnit.fromDescription("KB");

            // then
            assertAll(
                    () -> assertThat(mb).isEqualTo(AttachmentUnit.MB),
                    () -> assertThat(kb).isEqualTo(AttachmentUnit.KB)
            );
        }

        @Test
        void 존재하지_않는_설명이_주어지면_예외가_발생한다() {
            // given
            String invalidDescription = "GB";

            // when & then
            assertThatThrownBy(() -> AttachmentUnit.fromDescription(invalidDescription))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("각_단위의_임계값이_올바르게_설정되어_있다")
    void 각_단위의_임계값이_올바르게_설정되어_있다() {
        assertAll(
                () -> assertThat(AttachmentUnit.MB.getByteSize()).isEqualTo(1024L * 1024L),
                () -> assertThat(AttachmentUnit.KB.getByteSize()).isEqualTo(1024L)
        );
    }
}
