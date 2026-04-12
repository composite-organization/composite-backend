package kr.composite.api.vote.ui;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class VoteWidgetControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private Long createVoteWidget(boolean isAnonymous, boolean isMultiSelectable) {
        Map<String, Object> request = Map.of(
                "lessonId", 1L,
                "title", "테스트 투표",
                "isAnonymous", isAnonymous,
                "isMultiSelectable", isMultiSelectable,
                "options", List.of("옵션A", "옵션B")
        );

        ExtractableResponse<Response> response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/vote-widgets")
                .then()
                .statusCode(201)
                .extract();

        return response.jsonPath().getLong("id");
    }

    private void changeVoteStatus(Long voteWidgetId, String status) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("status", status))
                .when()
                .patch("/vote-widgets/{voteWidgetId}/status", voteWidgetId);
    }

    private List<Long> getOptionIds(Long voteWidgetId) {
        ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .get("/vote-widgets/{voteWidgetId}", voteWidgetId)
                .then()
                .extract();

        return response.jsonPath().getList("options.id", Long.class);
    }

    @Nested
    class 투표_위젯_생성_API {

        @Test
        void 투표_위젯을_생성하면_201을_응답한다() {
            // given
            Map<String, Object> request = Map.of(
                    "lessonId", 1L,
                    "title", "오늘 점심 뭐 먹을까?",
                    "isAnonymous", false,
                    "isMultiSelectable", false,
                    "options", List.of("칼국수", "비빔밥", "돈까스")
            );

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/vote-widgets")
                    .then()
                    .statusCode(201)
                    .body("id", notNullValue())
                    .body("title", equalTo("오늘 점심 뭐 먹을까?"))
                    .body("status", equalTo("IN_PROGRESS"))
                    .body("options", hasSize(3));
        }
    }

    @Nested
    class 투표_위젯_조회_API {

        @Test
        void IN_PROGRESS_익명_투표를_조회하면_anonymousOptions가_포함된다() {
            // given
            Long voteWidgetId = createVoteWidget(true, false);

            // when & then
            RestAssured.given()
                    .when()
                    .get("/vote-widgets/{voteWidgetId}", voteWidgetId)
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("IN_PROGRESS"))
                    .body("data.anonymousOptionStatuses", hasSize(2))
                    .body("data.totalParticipantCount", equalTo(0));
        }

        @Test
        void ENDED_상태의_투표를_조회하면_결과_데이터가_포함된다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);
            changeVoteStatus(voteWidgetId, "ENDED");

            // when & then
            RestAssured.given()
                    .when()
                    .get("/vote-widgets/{voteWidgetId}", voteWidgetId)
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("ENDED"))
                    .body("data.options", hasSize(2))
                    .body("data.selectedOptionIds", notNullValue());
        }

        @Test
        void 존재하지_않는_투표를_조회하면_404를_응답한다() {
            // when & then
            RestAssured.given()
                    .when()
                    .get("/vote-widgets/{voteWidgetId}", 9999L)
                    .then()
                    .statusCode(404);
        }
    }

    @Nested
    class 투표_상태_변경_API {

        @Test
        void IN_PROGRESS에서_ENDED로_변경하면_200을_응답한다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(Map.of("status", "ENDED"))
                    .when()
                    .patch("/vote-widgets/{voteWidgetId}/status", voteWidgetId)
                    .then()
                    .statusCode(200);
        }
    }

    @Nested
    @Sql(scripts = "/vote-test-student.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    class 투표_제출_API {

        @Test
        void 진행중인_투표에_제출하면_201을_응답한다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);
            List<Long> optionIds = getOptionIds(voteWidgetId);

            Map<String, Object> request = Map.of(
                    "studentId", 1L,
                    "optionIds", List.of(optionIds.get(0))
            );

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/vote-widgets/{voteWidgetId}/submissions", voteWidgetId)
                    .then()
                    .statusCode(201);
        }

        @Test
        void 진행중이_아닌_투표에_제출하면_400을_응답한다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);
            List<Long> optionIds = getOptionIds(voteWidgetId);
            changeVoteStatus(voteWidgetId, "ENDED");

            Map<String, Object> request = Map.of(
                    "studentId", 1L,
                    "optionIds", List.of(optionIds.get(0))
            );

            // when & then
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/vote-widgets/{voteWidgetId}/submissions", voteWidgetId)
                    .then()
                    .statusCode(400);
        }
    }

    @Nested
    class 투표_위젯_삭제_API {

        @Test
        void 투표_위젯을_삭제하면_204를_응답한다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);

            // when & then
            RestAssured.given()
                    .when()
                    .delete("/vote-widgets/{voteWidgetId}", voteWidgetId)
                    .then()
                    .statusCode(204);
        }

        @Test
        void 삭제된_투표_위젯을_조회하면_404를_응답한다() {
            // given
            Long voteWidgetId = createVoteWidget(false, false);
            RestAssured.given()
                    .delete("/vote-widgets/{voteWidgetId}", voteWidgetId);

            // when & then
            RestAssured.given()
                    .when()
                    .get("/vote-widgets/{voteWidgetId}", voteWidgetId)
                    .then()
                    .statusCode(404);
        }
    }
}
