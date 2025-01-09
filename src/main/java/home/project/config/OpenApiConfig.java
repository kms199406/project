package home.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
// BadRequest 응답 스키마
        Schema<?> badRequestResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(400));

        // Unauthorized 응답 스키마
        Schema<?> unauthorizedResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(401));

        // Forbidden 응답 스키마
        Schema<?> forbiddenResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(403));

        // Not Found 응답 스키마
        Schema<?> notFoundResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(404));

        // Conflict 응답 스키마
        Schema<?> conflictResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(409));

        // Internal Server Error 응답 스키마
        Schema<?> internalServerErrorResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("errorMessage", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(500));

        Schema<?> genericMapSchema = new ObjectSchema()
                .description("Key-Value 형태의 Map 객체입니다.")
                .additionalProperties(new Schema<>().type("string"))
                .example(Map.of("key1", "value1", "key2", "value2"));

        Schema<?> tokenResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("grantType", new Schema<>().type("string"))
                        .addProperty("accessToken", new Schema<>().type("string"))
                        .addProperty("refreshToken", new Schema<>().type("string"))
                        .addProperty("role", new Schema<>().type("string")))
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(200));

        Schema<?> productCouponResponseSchema = new ObjectSchema()
                .description("상품 쿠폰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(1))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 번호").example("PROD-2024-001"))
                        .addProperty("couponId", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(101))
                        .addProperty("issuedAt", new Schema<>().type("string").format("date-time").description("발급일").example("2024-01-01T00:00:00"))
                        .addProperty("usedAt", new Schema<>().type("string").format("date-time").description("사용일").example("2024-02-01T00:00:00"))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("isUsed", new Schema<>().type("boolean").description("사용 여부").example(false)))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("상품 쿠폰 조회 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        Schema<?> pagedEventListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 이벤트 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("이벤트 목록")
                                .items(new Schema<>().$ref("#/components/schemas/EventResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("전체 이벤트입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));
        // 회원가입 성공 응답 스키마
        Schema<?> memberJoinSuccessResponseSchema = new ObjectSchema()
                .description("회원가입 성공 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("accessToken", new Schema<>().type("string").description("액세스 토큰").example("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
                        .addProperty("refreshToken", new Schema<>().type("string").description("리프레시 토큰").example("dGhpc2lzYXJlZnJlc2h0b2tlbg=="))
                        .addProperty("role", new Schema<>().type("string").description("회원 권한").example("USER"))
                        .addProperty("successMessage", new Schema<>().type("string").description("성공 메시지").example("회원가입이 성공적으로 완료되었습니다.")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("회원가입 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 이벤트 응답 스키마
        Schema<?> eventResponseSchema = new ObjectSchema()
                .description("이벤트 응답 DTO")
                .addProperty("id", new Schema<>().type("integer").format("int64").description("이벤트 ID").example(1))
                .addProperty("name", new Schema<>().type("string").description("이벤트 이름").example("봄맞이 할인 이벤트"))
                .addProperty("description", new ArraySchema()
                        .description("이벤트 설명 목록")
                        .items(new Schema<>().type("string").example("봄 시즌 맞이 50% 할인")))
                .addProperty("startDate", new Schema<>().type("string").format("date-time").description("이벤트 시작 날짜").example("2024-03-01T00:00:00"))
                .addProperty("endDate", new Schema<>().type("string").format("date-time").description("이벤트 종료 날짜").example("2024-03-31T23:59:59"));


        // 회원 쿠폰 응답 스키마
        Schema<?> memberCouponResponseSchema = new ObjectSchema()
                .description("회원 쿠폰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("회원 쿠폰 ID").example(1))
                        .addProperty("memberEmail", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                        .addProperty("couponId", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(101))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(15))
                        .addProperty("issuedAt", new Schema<>().type("string").format("date-time").description("발급일").example("2024-01-01T00:00:00"))
                        .addProperty("usedAt", new Schema<>().type("string").format("date-time").description("사용일").example("2024-02-01T00:00:00"))
                        .addProperty("isUsed", new Schema<>().type("boolean").description("사용 여부").example(false)))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("회원 쿠폰 조회 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        // 페이지네이션된 상품 쿠폰 응답 스키마
        Schema<?> pagedProductCouponResponseSchema = new ObjectSchema()
                .description("페이지네이션된 상품 쿠폰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("상품 쿠폰 목록")
                                .items(new ObjectSchema()
                                        .addProperty("id", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(1))
                                        .addProperty("productNum", new Schema<>().type("string").description("상품 번호").example("PROD-2024-001"))
                                        .addProperty("couponId", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(101))
                                        .addProperty("issuedAt", new Schema<>().type("string").format("date-time").description("발급일").example("2024-01-01T00:00:00"))
                                        .addProperty("usedAt", new Schema<>().type("string").format("date-time").description("사용일").example("2024-02-01T00:00:00"))
                                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                                        .addProperty("isUsed", new Schema<>().type("boolean").description("사용 여부").example(false)))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("상품 쿠폰 조회 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

// 페이지네이션된 회원 쿠폰 응답 스키마
        Schema<?> pagedMemberCouponResponseSchema = new ObjectSchema()
                .description("페이지네이션된 회원 쿠폰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("회원 쿠폰 목록")
                                .items(new ObjectSchema()
                                        .addProperty("id", new Schema<>().type("integer").format("int64").description("회원 쿠폰 ID").example(1))
                                        .addProperty("memberEmail", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                                        .addProperty("couponId", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(101))
                                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(15))
                                        .addProperty("issuedAt", new Schema<>().type("string").format("date-time").description("발급일").example("2024-01-01T00:00:00"))
                                        .addProperty("usedAt", new Schema<>().type("string").format("date-time").description("사용일").example("2024-02-01T00:00:00"))
                                        .addProperty("isUsed", new Schema<>().type("boolean").description("사용 여부").example(false)))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("회원 쿠폰 조회 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 카테고리 생성 성공 응답 스키마
        Schema<?> categoryCreateSuccessResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("successMessage", new Schema<>().type("string").example("string(이)가 등록되었습니다.")))
                .addProperty("responseMessage", new Schema<>().type("string").example("카테고리 등록 성공"))
                .addProperty("status", new Schema<>().type("integer").example(200));

        // 카테고리 응답 스키마
        Schema<?> categoryResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("카테고리 ID").example(1))
                        .addProperty("code", new Schema<>().type("string").description("카테고리 코드").example("0001"))
                        .addProperty("name", new Schema<>().type("string").description("카테고리 이름").example("패션"))
                        .addProperty("level", new Schema<>().type("integer").description("카테고리 레벨").example(1))
                        .addProperty("parent", new Schema<>().type("integer").format("int64").description("부모 카테고리 ID").example(0))
                        .addProperty("children", new ArraySchema()
                                .description("하위 카테고리 목록")
                                .items(new Schema<>().$ref("#/components/schemas/CategoryResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        // 페이지네이션된 카테고리 목록 응답 스키마
        Schema<?> pagedCategoryListResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("카테고리 목록")
                                .items(new Schema<>().$ref("#/components/schemas/CategoryResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("전체 카테고리입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 일반 성공 응답 스키마
        Schema<?> generalSuccessResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("successMessage", new Schema<>().type("string").description("성공 메시지").example("string")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 쿠폰 응답 스키마
        Schema<?> couponResponseSchema = new ObjectSchema()
                .description("쿠폰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("쿠폰 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("쿠폰 이름").example("할인 쿠폰"))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("startDate", new Schema<>().type("string").format("date-time").description("쿠폰 시작 날짜").example("2024-01-01T00:00:00"))
                        .addProperty("endDate", new Schema<>().type("string").format("date-time").description("쿠폰 종료 날짜").example("2024-12-31T23:59:59"))
                        .addProperty("assignBy", new Schema<>().type("string").description("발급자").example("관리자"))
                        .addProperty("productCouponResponse", new ArraySchema()
                                .description("상품 쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductCouponResponse")))
                        .addProperty("memberCouponResponse", new ArraySchema()
                                .description("회원 쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/MemberCouponResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("쿠폰 정보가 성공적으로 반환되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 페이지네이션된 쿠폰 목록 응답 스키마
        Schema<?> pagedCouponListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 쿠폰 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/CouponResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("전체 쿠폰입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 회원 응답 스키마
        // 회원 응답 스키마
        Schema<?> memberResponseSchema = new ObjectSchema()
                .description("회원 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("회원 ID").example(1))
                        .addProperty("email", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                        .addProperty("name", new Schema<>().type("string").description("회원 이름").example("홍길동"))
                        .addProperty("phone", new Schema<>().type("string").description("회원 전화번호").example("01012345678"))
                        .addProperty("role", new Schema<>().type("string").description("회원 권한").example("ADMIN"))
                        .addProperty("gender", new Schema<>().type("string").description("회원 성별").example("F"))
                        .addProperty("birthDate", new Schema<>().type("string").format("date").description("회원 생년월일").example("1990-01-01"))
                        .addProperty("defaultAddress", new Schema<>().type("string").description("기본 주소").example("서울특별시 강남구 테헤란로"))
                        .addProperty("secondAddress", new Schema<>().type("string").description("보조 주소 1").example("서울특별시 서초구 서초대로"))
                        .addProperty("thirdAddress", new Schema<>().type("string").description("보조 주소 2").example("서울특별시 송파구 올림픽로"))
                        .addProperty("grade", new Schema<>().type("string").description("회원 등급").example("GOLD"))
                        .addProperty("point", new Schema<>().type("integer").format("int64").description("회원 포인트").example(1000))
                        .addProperty("memberCouponResponse", new ArraySchema()
                                .description("회원 쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/MemberCouponResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));
        // 페이지네이션된 회원 목록 응답 스키마
        Schema<?> pagedMemberListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 회원 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("회원 목록")
                                .items(new Schema<>().$ref("#/components/schemas/MemberResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("전체 회원입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 본인 확인 성공 응답 스키마
        Schema<?> verifyResponseSchema = new ObjectSchema()
                .description("본인 확인 성공 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("verificationToken", new Schema<>().type("string").description("본인 확인 토큰").example("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
                        .addProperty("successMessage", new Schema<>().type("string").description("성공 메시지").example("본인 확인이 완료되었습니다.")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("본인 확인 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 회원 유효성 검사 실패 응답 스키마
        Schema<?> memberValidationFailedResponseSchema = new ObjectSchema()
                .addProperty("result", new ObjectSchema()
                        .addProperty("email", new Schema<>().type("string"))
                        .addProperty("password", new Schema<>().type("string"))
                        .addProperty("passwordConfirm", new Schema<>().type("string"))
                        .addProperty("phone", new Schema<>().type("string"))
                        .addProperty("name", new Schema<>().type("string"))
                )
                .addProperty("responseMessage", new Schema<>().type("string"))
                .addProperty("status", new Schema<>().type("integer").example(400));

        // 알림 응답 스키마
        Schema<?> notificationResponseSchema = new ObjectSchema()
                .description("알림 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("알림 ID").example(1))
                        .addProperty("memberId", new Schema<>().type("integer").format("int64").description("회원 ID").example(101))
                        .addProperty("notificationType", new Schema<>().type("string").description("알림 유형").example("ORDER_COMPLETE"))
                        .addProperty("description", new Schema<>().type("string").description("알림 설명").example("주문이 완료되었습니다."))
                        .addProperty("isRead", new Schema<>().type("boolean").description("읽음 여부").example(false))
                        .addProperty("createdAt", new Schema<>().type("string").format("date-time").description("생성일").example("2024-01-01T12:00:00")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));
        // 페이지네이션된 알림 목록 응답 스키마
        Schema<?> pagedNotificationListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 알림 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("알림 목록")
                                .items(new Schema<>().$ref("#/components/schemas/NotificationResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("모든 알림입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 주문 응답 스키마
        Schema<?> orderResponseSchema = new ObjectSchema()
                .description("주문 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("주문 ID").example(1))
                        .addProperty("orderNum", new Schema<>().type("string").description("주문 번호").example("ORD-2024-0001"))
                        .addProperty("orderDate", new Schema<>().type("string").format("date-time").description("주문 날짜").example("2024-01-01T10:00:00"))
                        .addProperty("deliveryAddress", new Schema<>().type("string").description("배송 주소").example("서울특별시 강남구 테헤란로"))
                        .addProperty("totalAmount", new Schema<>().type("integer").format("int64").description("총 금액").example(50000))
                        .addProperty("pointsUsed", new Schema<>().type("integer").format("int64").description("사용 포인트").example(500))
                        .addProperty("pointsEarned", new Schema<>().type("integer").format("int64").description("적립 포인트").example(100))
                        .addProperty("products", new ArraySchema()
                                .description("주문한 상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductDTOForOrder"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("주문 응답입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 페이지네이션된 주문 목록 응답 스키마
        Schema<?> pagedOrderListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 주문 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 주문 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("주문 목록")
                                .items(new Schema<>().$ref("#/components/schemas/OrderResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("주문 목록입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // ProductWithQnAAndReviewResponseForManager 스키마
        Schema<?> productWithQnAAndReviewResponseForManagerSchema = new ObjectSchema()
                .description("관리자용 상품 QnA 및 리뷰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("상품 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("상품 이름").example("블루 데님 자켓"))
                        .addProperty("brand", new Schema<>().type("string").description("브랜드 이름").example("리바이스"))
                        .addProperty("category", new Schema<>().type("string").description("카테고리").example("아우터/자켓"))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 고유 번호").example("PROD-2024-001"))
                        .addProperty("stock", new Schema<>().type("integer").format("int64").description("재고 수량").example(100))
                        .addProperty("soldQuantity", new Schema<>().type("integer").format("int64").description("판매 수량").example(50))
                        .addProperty("price", new Schema<>().type("integer").format("int64").description("가격").example(89000))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("defectiveStock", new Schema<>().type("integer").format("int64").description("불량 재고 수량").example(2))
                        .addProperty("description", new ArraySchema()
                                .description("상품 설명")
                                .items(new Schema<>().type("string").example("상품 설명 예시")))
                        .addProperty("createProductDate", new Schema<>().type("string").format("date-time").description("상품 등록일시").example("2024-01-01T10:00:00"))
                        .addProperty("mainImageFile", new Schema<>().type("string").description("상품 이미지 URL").example("https://example.com/image.jpg"))
                        .addProperty("size", new Schema<>().type("string").description("사이즈").example("M"))
                        .addProperty("color", new Schema<>().type("string").description("색상").example("블루"))
                        .addProperty("qnADetailResponses", new ArraySchema()
                                .description("QnA 상세 목록")
                                .items(new Schema<>().$ref("#/components/schemas/QnADetailResponse")))
                        .addProperty("reviewDetailResponses", new ArraySchema()
                                .description("리뷰 상세 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ReviewDetailResponse")))
                        .addProperty("productCouponResponse", new ArraySchema()
                                .description("상품 쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductCouponResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // ProductSimpleResponseForManager 스키마
        Schema<?> productSimpleResponseForManagerSchema = new ObjectSchema()
                .description("관리자용 상품 간단 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("상품 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("상품 이름").example("블루 데님 자켓"))
                        .addProperty("brand", new Schema<>().type("string").description("브랜드 이름").example("리바이스"))
                        .addProperty("stock", new Schema<>().type("integer").format("int64").description("재고 수량").example(100))
                        .addProperty("soldQuantity", new Schema<>().type("integer").format("int64").description("판매 수량").example(50))
                        .addProperty("price", new Schema<>().type("integer").format("int64").description("가격").example(89000))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("createProductDate", new Schema<>().type("string").format("date-time").description("생성일").example("2024-01-01T10:00:00"))
                        .addProperty("size", new Schema<>().type("string").description("사이즈").example("M"))
                        .addProperty("color", new Schema<>().type("string").description("색상").example("블루")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 페이지네이션된 상품 목록 응답 스키마
        Schema<?> pagedProductListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 상품 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 상품 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductSimpleResponseForManager"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("귀 판매자가 관리하는 전체 상품입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> pagedNewProductListResponseSchema = new ObjectSchema()
                .description("페이지네이션된 신상품 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 신상품 수").example(20))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("신상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductSimpleResponseForManager"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("가장 최근 추가된 신상품 20개입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // ProductResponseForManager 스키마
        Schema<?> productResponseForManagerSchema = new ObjectSchema()
                .description("관리자용 상품 상세 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("상품 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("상품명").example("블루 데님 자켓"))
                        .addProperty("brand", new Schema<>().type("string").description("브랜드명").example("리바이스"))
                        .addProperty("category", new Schema<>().type("string").description("카테고리").example("아우터/자켓"))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 고유번호").example("PROD-2024-001"))
                        .addProperty("stock", new Schema<>().type("integer").format("int64").description("재고 수량").example(100))
                        .addProperty("soldQuantity", new Schema<>().type("integer").format("int64").description("판매 수량").example(50))
                        .addProperty("price", new Schema<>().type("integer").format("int64").description("가격").example(89000))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("defectiveStock", new Schema<>().type("integer").format("int64").description("불량 재고 수량").example(2))
                        .addProperty("description", new ArraySchema()
                                .description("상품 설명")
                                .items(new Schema<>().type("string").example("상품 설명 예시")))
                        .addProperty("createProductDate", new Schema<>().type("string").format("date-time").description("상품 등록일시").example("2024-01-01T10:00:00"))
                        .addProperty("mainImageFile", new Schema<>().type("string").description("상품 이미지 URL").example("https://example.com/image.jpg"))
                        .addProperty("size", new Schema<>().type("string").description("사이즈").example("M"))
                        .addProperty("color", new Schema<>().type("string").description("색상").example("블루"))
                        .addProperty("productCouponResponse", new ArraySchema()
                                .description("상품 쿠폰 정보 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductCouponResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // 페이지네이션된 상품 검색 결과 응답 스키마
        Schema<?> pagedProductSearchResponseSchema = new ObjectSchema()
                .description("페이지네이션된 상품 검색 결과 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 상품 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductResponseForManager"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("검색 조건에 맞는 상품입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // BrandListResponseSchema
        Schema<?> brandListResponseSchema = new ObjectSchema()
                .description("브랜드 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 브랜드 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("브랜드 이름 목록")
                                .items(new Schema<>().type("string").example("리바이스"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("전체 브랜드 입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // ProductResponseSchema
        Schema<?> productResponseSchema = new ObjectSchema()
                .description("상품 상세 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("상품 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("상품명").example("블루 데님 자켓"))
                        .addProperty("brand", new Schema<>().type("string").description("브랜드명").example("리바이스"))
                        .addProperty("category", new Schema<>().type("string").description("카테고리").example("아우터/자켓"))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 고유번호").example("PROD-2024-001"))
                        .addProperty("stock", new Schema<>().type("integer").format("int64").description("재고 수량").example(100))
                        .addProperty("soldQuantity", new Schema<>().type("integer").format("int64").description("판매 수량").example(50))
                        .addProperty("price", new Schema<>().type("integer").format("int64").description("가격").example(89000))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("description", new ArraySchema()
                                .description("상품 설명")
                                .items(new Schema<>().type("string").example("상품 설명 예시")))
                        .addProperty("mainImageFile", new Schema<>().type("string").description("상품 이미지 URL").example("https://example.com/image.jpg"))
                        .addProperty("size", new Schema<>().type("string").description("사이즈").example("M"))
                        .addProperty("color", new Schema<>().type("string").description("색상").example("블루")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("상품 정보가 수정되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // QnADetailResponseSchema
        Schema<?> qnADetailResponseSchema = new ObjectSchema()
                .description("QnA 상세 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("QnA ID").example(1))
                        .addProperty("qnAType", new Schema<>().type("string").description("QnA 유형").example("PRODUCT"))
                        .addProperty("subject", new Schema<>().type("string").description("제목").example("상품 배송 문의"))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 번호").example("PROD-2024-001"))
                        .addProperty("orderNum", new Schema<>().type("string").description("주문 번호").example("ORD-2024-0001"))
                        .addProperty("description", new Schema<>().type("string").description("내용").example("상품 배송은 언제 되나요?"))
                        .addProperty("memberEmail", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                        .addProperty("createAt", new Schema<>().type("string").format("date-time").description("생성일").example("2024-01-01T10:00:00"))
                        .addProperty("answer", new Schema<>().type("string").description("답변").example("상품 배송은 내일 완료될 예정입니다."))
                        .addProperty("answerDate", new Schema<>().type("string").format("date-time").description("답변 날짜").example("2024-01-02T10:00:00"))
                        .addProperty("answererEmail", new Schema<>().type("string").description("답변자 이메일").example("admin@example.com"))
                        .addProperty("answerStatus", new Schema<>().type("string").description("답변 상태").example("ANSWERED")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("QnA 상세 정보입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // PagedQnAListResponseSchema
        Schema<?> qnAResponseSchema = new ObjectSchema()
                .description("QnA 간단 응답 DTO")
                .addProperty("qnAId", new Schema<>().type("integer").format("int64").description("QnA ID").example(1))
                .addProperty("qnAType", new Schema<>().type("string").description("QnA 유형").example("PRODUCT"))
                .addProperty("subject", new Schema<>().type("string").description("제목").example("상품 배송 문의"))
                .addProperty("memberEmail", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                .addProperty("createAt", new Schema<>().type("string").format("date-time").description("생성일").example("2024-01-01T10:00:00"))
                .addProperty("answerStatus", new Schema<>().type("string").description("답변 상태").example("ANSWERED"));

        Schema<?> pagedQnAListResponseSchema = new ObjectSchema()
                .description("페이징된 QnA 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 QnA 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("QnA 목록")
                                .items(new Schema<>().$ref("#/components/schemas/QnAResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("모든 QnA 입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> pagedSoldProductListResponseSchema = new ObjectSchema()
                .description("페이징된 판매 상품 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 판매 상품 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("판매 상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductResponseForManager"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("판매 상품 목록입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> pagedSalesRankingResponseSchema = new ObjectSchema()
                .description("페이징된 판매순위 상품 목록 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 상품 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("판매순위 상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("판매수량이 많은순으로 상품이 조회됩니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // ShippingResponseSchema
        Schema<?> shippingResponseSchema = new ObjectSchema()
                .description("배송 응답 데이터")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("배송 ID").example(1))
                        .addProperty("deliveryNum", new Schema<>().type("string").description("배송 번호").example("DEL-2024-0001"))
                        .addProperty("orderDate", new Schema<>().type("string").format("date-time").description("주문 날짜").example("2024-01-01T10:00:00"))
                        .addProperty("deliveryAddress", new Schema<>().type("string").description("배송 주소").example("서울특별시 강남구 테헤란로"))
                        .addProperty("totalAmount", new Schema<>().type("integer").format("int64").description("총 금액").example(100000))
                        .addProperty("products", new ArraySchema()
                                .description("배송 상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductDTOForOrder")))
                        .addProperty("deliveryType", new Schema<>().type("string").description("배송 유형").example("EXPRESS"))
                        .addProperty("arrivedDate", new Schema<>().type("string").description("도착 날짜").example("2024-12-31"))
                        .addProperty("departureDate", new Schema<>().type("string").description("출발 날짜").example("2024-12-01"))
                        .addProperty("deliveryStatusType", new Schema<>().type("string").description("배송 상태").example("IN_TRANSIT"))
                        .addProperty("deliveryCost", new Schema<>().type("integer").format("int64").description("배송비").example(3000))
                        .addProperty("memberEmail", new Schema<>().type("string").description("회원 이메일").example("user@example.com")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("배송 데이터입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        // PagedShippingResponseSchema
        Schema<?> pagedShippingResponseSchema = new ObjectSchema()
                .description("페이징된 배송 목록 응답")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 배송 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("배송 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ShippingResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("배송 목록입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> assignRoleResponseSchema = new ObjectSchema()
                .description("역할 할당 응답 스키마")
                .addProperty("result", new ObjectSchema()
                        .addProperty("authority", new Schema<>().type("string").description("할당된 역할 타입").example("admin"))
                        .addProperty("successMessage", new Schema<>().type("string").description("성공 메시지").example("회원 ID 123이(가) 관리자 역할로 할당되었습니다.")))
                .addProperty("responseMessage", new Schema<>().type("string").description("API 응답 메시지").example("역할이 성공적으로 할당되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> pagedUserRoleListResponseSchema = new ObjectSchema()
                .description("사용자와 그들의 역할 목록(페이지네이션)")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 역할 수").example(50))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("사용자와 그들의 역할 목록")
                                .items(new ObjectSchema()
                                        .description("사용자 역할 정보")
                                        .addProperty("id", new Schema<>().type("integer").format("int64").description("역할 ID").example(1))
                                        .addProperty("role", new Schema<>().type("string").description("사용자의 역할 타입").example("ADMIN"))
                                        .addProperty("name", new Schema<>().type("string").description("사용자 이름").example("홍길동")))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("모든 사용자의 역할 목록입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> cartResponseSchema = new ObjectSchema()
                .description("장바구니 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .description("장바구니 상세 정보를 포함한 응답 결과")
                        .addProperty("email", new Schema<>()
                                .type("string")
                                .description("사용자 이메일 주소")
                                .example("example@example.com"))
                        .addProperty("products", new ArraySchema()
                                .description("주문을 위한 장바구니 내 상품 목록")
                                .items(new ObjectSchema()
                                        .$ref("#/components/schemas/ProductDTOForOrderSchema"))))
                .addProperty("responseMessage", new Schema<>()
                        .type("string")
                        .description("응답 메시지")
                        .example("장바구니를 성공적으로 가져왔습니다."))
                .addProperty("status", new Schema<>()
                        .type("integer")
                        .description("HTTP 상태 코드")
                        .example(200));

        Schema<?> productDTOForOrderSchema = new ObjectSchema()
                .description("주문용 상품 DTO")
                .addProperty("productId", new Schema<>()
                        .type("integer")
                        .format("int64")
                        .description("상품의 고유 식별자")
                        .example(101))
                .addProperty("price", new Schema<>()
                        .type("integer")
                        .format("int64")
                        .description("상품의 단위당 가격")
                        .example(20000))
                .addProperty("quantity", new Schema<>()
                        .type("integer")
                        .description("장바구니 내 상품의 수량")
                        .example(2));

        Schema<?> pagedProductSimpleResponseForCartSchema = new ObjectSchema()
                .description("사용자 장바구니 내 상품(페이지네이션) 응답 스키마")
                .addProperty("result", new ObjectSchema()
                        .description("장바구니 항목이 포함된 페이지네이션 결과")
                        .addProperty("totalCount", new Schema<>()
                                .type("integer")
                                .format("int64")
                                .description("장바구니 내 전체 상품 수")
                                .example(10))
                        .addProperty("page", new Schema<>()
                                .type("integer")
                                .description("현재 페이지 번호")
                                .example(1))
                        .addProperty("content", new ArraySchema()
                                .description("현재 페이지의 장바구니 내 상품 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductSimpleResponseForCartSchema"))))
                .addProperty("responseMessage", new Schema<>()
                        .type("string")
                        .description("결과를 설명하는 응답 메시지")
                        .example("장바구니 항목을 성공적으로 가져왔습니다."))
                .addProperty("status", new Schema<>()
                        .type("integer")
                        .description("응답의 HTTP 상태 코드")
                        .example(200));

        Schema<?> pagedEventImageListResponseSchema = new ObjectSchema()
                .description("이벤트 이미지 목록(페이지네이션) 응답 스키마")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 항목 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("이벤트 이미지 목록")
                                .items(new Schema<>().$ref("#/components/schemas/EventSimpleResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("모든 이벤트 이미지를 성공적으로 가져왔습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        Schema<?> memberResponseForUserSchema = new ObjectSchema()
                .description("사용자 상세 응답 DTO")
                .addProperty("id", new Schema<>().type("integer").format("int64").description("회원 ID").example(1))
                .addProperty("email", new Schema<>().type("string").description("회원 이메일").example("user@example.com"))
                .addProperty("name", new Schema<>().type("string").description("회원 이름").example("홍길동"))
                .addProperty("phone", new Schema<>().type("string").description("회원 전화번호").example("01012345678"))
                .addProperty("gender", new Schema<>().type("string").description("회원 성별").example("MALE"))
                .addProperty("birthDate", new Schema<>().type("string").format("date").description("회원 생년월일").example("1990-01-01"))
                .addProperty("defaultAddress", new Schema<>().type("string").description("기본 주소").example("서울특별시 강남구 테헤란로"))
                .addProperty("secondAddress", new Schema<>().type("string").description("보조 주소").example("서울특별시 서초구 서초대로"))
                .addProperty("thirdAddress", new Schema<>().type("string").description("추가 주소").example("서울특별시 송파구 올림픽로"))
                .addProperty("grade", new Schema<>().type("string").description("회원 등급").example("GOLD"))
                .addProperty("point", new Schema<>().type("integer").format("int64").description("회원 포인트").example(1000))
                .addProperty("memberCouponResponse", new ArraySchema()
                        .description("회원 쿠폰 목록")
                        .items(new Schema<>().$ref("#/components/schemas/MemberCouponResponse")))
                .addProperty("successMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> notificationDetailResponseSchema = new ObjectSchema()
                .description("알림 상세 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("notificationType", new Schema<>()
                                .type("string")
                                .description("알림 유형")
                                .example("ORDER_COMPLETE"))
                        .addProperty("description", new Schema<>()
                                .type("string")
                                .description("알림 설명")
                                .example("주문이 완료되었습니다."))
                        .addProperty("createdAt", new Schema<>()
                                .type("string")
                                .format("date-time")
                                .description("알림 생성 시간")
                                .example("2025-01-01T12:00:00")))
                .addProperty("responseMessage", new Schema<>()
                        .type("string")
                        .description("응답 메시지")
                        .example("알림 세부 정보 조회 성공"))
                .addProperty("status", new Schema<>()
                        .type("integer")
                        .description("HTTP 상태 코드")
                        .example(200));

        Schema<?> pagedOrderResponseSchema = new ObjectSchema()
                .description("페이지네이션된 주문 응답 스키마")
                .addProperty("result", new ObjectSchema()
                        .description("주문 목록이 포함된 페이지네이션 결과")
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 주문 수").example(25))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("현재 페이지의 주문 목록")
                                .items(new Schema<>().$ref("#/components/schemas/OrderResponseSchema"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("내 주문 목록입니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> productWithQnAAndReviewResponseSchema = new ObjectSchema()
                .description("상품 QnA 및 리뷰 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("상품 ID").example(1))
                        .addProperty("name", new Schema<>().type("string").description("상품 이름").example("블루 데님 자켓"))
                        .addProperty("brand", new Schema<>().type("string").description("브랜드 이름").example("리바이스"))
                        .addProperty("category", new Schema<>().type("string").description("카테고리").example("아우터/자켓"))
                        .addProperty("productNum", new Schema<>().type("string").description("상품 고유 번호").example("PROD-2024-001"))
                        .addProperty("price", new Schema<>().type("integer").format("int64").description("가격").example(89000))
                        .addProperty("discountRate", new Schema<>().type("integer").description("할인율").example(20))
                        .addProperty("description", new ArraySchema()
                                .description("상품 설명")
                                .items(new Schema<>().type("string").example("이 상품은 고급 데님 소재로 만들어졌습니다.")))
                        .addProperty("mainImageFile", new Schema<>().type("string").description("이미지 URL").example("https://example.com/image.jpg"))
                        .addProperty("isLiked", new Schema<>().type("boolean").description("좋아요 여부").example(true))
                        .addProperty("size", new Schema<>().type("string").description("사이즈").example("M"))
                        .addProperty("color", new Schema<>().type("string").description("색상").example("블루"))
                        .addProperty("productCouponResponses", new ArraySchema()
                                .description("상품 쿠폰 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ProductCouponResponseSchema")))
                        .addProperty("qnADetailResponses", new ArraySchema()
                                .description("QnA 상세 목록")
                                .items(new Schema<>().$ref("#/components/schemas/QnADetailResponseSchema")))
                        .addProperty("reviewDetailResponses", new ArraySchema()
                                .description("리뷰 상세 목록")
                                .items(new Schema<>().$ref("#/components/schemas/ReviewDetailResponseSchema"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("상품 세부 정보 조회 성공"))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));

        Schema<?> wishListResponseSchema = new ObjectSchema()
                .description("위시리스트 응답 DTO")
                .addProperty("result", new ObjectSchema()
                        .addProperty("id", new Schema<>().type("integer").format("int64").description("위시리스트 ID").example(1))
                        .addProperty("productId", new Schema<>().type("integer").format("int64").description("상품 ID").example(1001))
                        .addProperty("liked", new Schema<>().type("boolean").description("좋아요 여부").example(true))
                        .addProperty("message", new Schema<>().type("string").description("메시지").example("위시리스트에 추가되었습니다.")))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("요청이 성공적으로 처리되었습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        Schema<?> pagedQnAResponseSchema = new ObjectSchema()
                .description("페이지네이션된 QnA 응답 스키마")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64").description("전체 QnA 수").example(100))
                        .addProperty("page", new Schema<>().type("integer").description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("QnA 목록")
                                .items(new Schema<>().$ref("#/components/schemas/QnAResponseSchema"))))
                .addProperty("responseMessage", new Schema<>().type("string").description("응답 메시지").example("QnA 목록을 성공적으로 가져왔습니다."))
                .addProperty("status", new Schema<>().type("integer").description("HTTP 상태 코드").example(200));


        Schema<?> pagedWishListResponseSchema = new ObjectSchema()
                .description("위시리스트 페이지네이션 응답")
                .addProperty("result", new ObjectSchema()
                        .addProperty("totalCount", new Schema<>().type("integer").format("int64")
                                .description("전체 위시리스트 수").example(50))
                        .addProperty("page", new Schema<>().type("integer")
                                .description("현재 페이지 번호").example(1))
                        .addProperty("content", new ArraySchema()
                                .description("위시리스트 목록")
                                .items(new Schema<>().$ref("#/components/schemas/WishListResponse"))))
                .addProperty("responseMessage", new Schema<>().type("string")
                        .description("응답 메시지").example("내 모든 wishList 입니다."))
                .addProperty("status", new Schema<>().type("integer")
                        .description("HTTP 상태 코드").example(200));

        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Bearer Token");

        return new OpenAPI()
                .info(new Info()
                        .title("예제 게시판 Swagger")
                        .version("1.0.0")
                        .description("API 문서입니다."))
                .components(new Components()
                        .addSchemas("BadRequestResponseSchema", badRequestResponseSchema)
                        .addSchemas("UnauthorizedResponseSchema", unauthorizedResponseSchema)
                        .addSchemas("ForbiddenResponseSchema", forbiddenResponseSchema)
                        .addSchemas("NotFoundResponseSchema", notFoundResponseSchema)
                        .addSchemas("ConflictResponseSchema", conflictResponseSchema)
                        .addSchemas("InternalServerErrorResponseSchema", internalServerErrorResponseSchema)
                        .addSchemas("TokenResponseSchema", tokenResponseSchema)
                        .addSchemas("GenericMapSchemaSchema", genericMapSchema)
                        .addSchemas("CategoryCreateSuccessResponseSchema", categoryCreateSuccessResponseSchema)
                        .addSchemas("CategoryResponseSchema", categoryResponseSchema)
                        .addSchemas("PagedCategoryListResponseSchema", pagedCategoryListResponseSchema)
                        .addSchemas("GeneralSuccessResponseSchema", generalSuccessResponseSchema)
                        .addSchemas("CouponResponseSchema", couponResponseSchema)
                        .addSchemas("PagedCouponListResponseSchema", pagedCouponListResponseSchema)
                        .addSchemas("MemberCouponResponse", memberCouponResponseSchema)
                        .addSchemas("ProductCouponResponse", productCouponResponseSchema)
                        .addSchemas("EventResponseSchema", eventResponseSchema)
                        .addSchemas("PagedEventListResponseSchema", pagedEventListResponseSchema)
                        .addSchemas("MemberJoinSuccessResponseSchema", memberJoinSuccessResponseSchema)
                        .addSchemas("MemberResponseSchema", memberResponseSchema)
                        .addSchemas("PagedMemberListResponseSchema", pagedMemberListResponseSchema)
                        .addSchemas("VerifyResponseSchema", verifyResponseSchema)
                        .addSchemas("NotificationResponseSchema", notificationResponseSchema)
                        .addSchemas("PagedNotificationListResponseSchema", pagedNotificationListResponseSchema)
                        .addSchemas("NotificationResponse", notificationResponseSchema)
                        .addSchemas("OrderResponseSchema", orderResponseSchema)
                        .addSchemas("PagedOrderListResponseSchema", pagedOrderListResponseSchema)
                        .addSchemas("ProductWithQnAAndReviewResponseForManagerSchema", productWithQnAAndReviewResponseForManagerSchema)
                        .addSchemas("ProductSimpleResponseForManager", productSimpleResponseForManagerSchema)
                        .addSchemas("PagedProductListResponseSchema", pagedProductListResponseSchema)
                        .addSchemas("PagedNewProductListResponseSchema", pagedNewProductListResponseSchema)
                        .addSchemas("ProductResponseForManager", productResponseForManagerSchema)
                        .addSchemas("PagedProductSearchResponseSchema", pagedProductSearchResponseSchema)
                        .addSchemas("BrandListResponseSchema", brandListResponseSchema)
                        .addSchemas("ProductResponseSchema", productResponseSchema)
                        .addSchemas("QnADetailResponseSchema", qnADetailResponseSchema)
                        .addSchemas("QnAResponse", qnAResponseSchema)
                        .addSchemas("PagedQnAListResponseSchema", pagedQnAListResponseSchema)
                        .addSchemas("PagedSoldProductListResponseSchema", pagedSoldProductListResponseSchema)
                        .addSchemas("PagedSalesRankingResponseSchema", pagedSalesRankingResponseSchema)
                        .addSchemas("ShippingResponseSchema", shippingResponseSchema)
                        .addSchemas("PagedShippingResponseSchema", pagedShippingResponseSchema)
                        .addSchemas("PagedProductCouponResponseSchema", pagedProductCouponResponseSchema)
                        .addSchemas("PagedMemberCouponResponseSchema", pagedMemberCouponResponseSchema)
                        .addSchemas("AssignRoleResponseSchema", assignRoleResponseSchema)
                        .addSchemas("PagedUserRoleListResponseSchema", pagedUserRoleListResponseSchema)
                        .addSchemas("CartResponseSchema", cartResponseSchema)
                        .addSchemas("ProductDTOForOrderSchema", productDTOForOrderSchema)
                        .addSchemas("PagedProductSimpleResponseForCartSchema", pagedProductSimpleResponseForCartSchema)
                        .addSchemas("PagedEventImageListResponseSchema", pagedEventImageListResponseSchema)
                        .addSchemas("MemberResponseForUserSchema", memberResponseForUserSchema)
                        .addSchemas("NotificationDetailResponseSchema", notificationDetailResponseSchema)
                        .addSchemas("PagedOrderResponseSchema", pagedOrderResponseSchema)
                        .addSchemas("ProductWithQnAAndReviewResponseSchema", productWithQnAAndReviewResponseSchema)
                        .addSchemas("PagedQnAResponseSchema", pagedQnAResponseSchema)
                        .addSchemas("WishListResponseSchema", wishListResponseSchema)
                        .addSchemas("PagedWishListResponseSchema", pagedWishListResponseSchema)


                        .addSecuritySchemes("bearerAuth", apiKey))
                .addSecurityItem(securityRequirement);
    }
}