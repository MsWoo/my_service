package ms.toy.my_service.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import ms.toy.my_service.domain.entity.Member;
import ms.toy.my_service.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Member> memberList = new ArrayList<>();

    private long startTime;

    public int batchSize = 100;
//    private int batchSize = 1000;
//    private int batchSize = 10000;


    /**
     * 테스트용 데이터 생성 및 시작 시간 설정
     */
    @BeforeEach
    void beforeEachTest() {
        for (int i=1; i<=100; i++) {
            LocalDateTime randomDateTime = this.generateRandomDateTime();

            memberList.add(Member.builder()
                    .userId("TID" + i)
                    .userName("TName" + i)
                    .phoneNumber("010-0000-0000")
                    .password("password")
                    .authorityId(1L)
                    .createdAt(randomDateTime)
                    .build());
        }

        startTime = System.currentTimeMillis();
    }

    /**
     * 테스트 종료 후 elapsedTime 출력
     */
    @AfterEach
    void afterEachTest() {
        System.out.println("처리 시간 : " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Test
    @DisplayName("Spring Data Jpa - saveAll()")
    @Rollback(value = false)
    void Jpa_saveAll() {
        System.out.println("--- saveAll() Start ---");

        memberRepository.saveAll(memberList);

        System.out.println("--- saveAll() End ---");
    }


    @Test
    @DisplayName("JdbcTemplate - batchUpdate()")
    @Rollback(value = false)
    void JdbcTemplate_batchUpdate() {
        System.out.println("--- batchUpdate() Start ---");

        int batchCount = 0;

        List<Member> subMemberList = new ArrayList<>();

        for (int i=0; i< memberList.size(); i++) {
            subMemberList.add(memberList.get(i));

            if ((i + 1) % this.batchSize == 0) {
                batchCount = this.batchUpdate(batchCount, subMemberList);
                subMemberList.clear();
            }
        }

        if (!subMemberList.isEmpty()) {
            batchCount = this.batchUpdate(batchCount, subMemberList);
        }

        System.out.println("BatchCount : " + batchCount);
        System.out.println("--- batchUpdate() End ---");
    }

    @Test
    @DisplayName("JdbcTemplate - executeBatch()")
    @Rollback(value = false)
    void JdbcTemplate_preparedStatement_excuteBatch() {
        System.out.println("--- excuteBatch() Start ---");

        Connection con = null;
        PreparedStatement pstmt = null;

        String bulkInsertQuery = "INSERT INTO member (user_id, user_name, phone_number, password, authority_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        int rowNum = 0;

        try {
            con = jdbcTemplate.getDataSource().getConnection();
            pstmt = con.prepareStatement(bulkInsertQuery);

            for (int i=0; i< memberList.size(); i++) {
                pstmt.setString(1, memberList.get(i).getUserId());
                pstmt.setString(2, memberList.get(i).getUserName());
                pstmt.setString(3, memberList.get(i).getPhoneNumber());
                pstmt.setString(4, memberList.get(i).getPassword());
                pstmt.setLong(5, memberList.get(i).getAuthorityId());
                pstmt.setTimestamp(6, Timestamp.valueOf(memberList.get(i).getCreatedAt()));

                pstmt.addBatch();
                pstmt.clearParameters();

                if ((i+1) % this.batchSize == 0) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    rowNum++;
                }
            }

            pstmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {pstmt.close();pstmt = null;} catch(SQLException ex){}
            if (con != null) try {con.close();con = null;} catch(SQLException ex){}
        }

        System.out.println("BatchCount : " + rowNum);
        System.out.println("--- excuteBatch() End ---");
    }

    @Test
    @DisplayName("Spring Data Jpa - findAllByCreatedAtIsBetween()")
    void Jpa_findByCreatedAt() {
        System.out.println("--- findAllByCreatedAtIsBetween() Start ---");

        List<Member> list = memberRepository.findAllByCreatedAtIsBetween(
                LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0),
                LocalDateTime.of(2023, Month.JANUARY, 31, 23, 59));

        System.out.println("List Size : " + list.size());

        System.out.println("--- findAllByCreatedAtIsBetween() End ---");
    }


    /**
     * JdbcTemplate에서 제공하는 batchUpdate 메소드
     * @param batchCount
     * @param subMemberList
     * @return
     */
    private int batchUpdate(int batchCount, List<Member> subMemberList) {
        String bulkInsertQuery = "INSERT INTO member (user_id, user_name, phone_number, password, authority_id, created_at) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(bulkInsertQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, memberList.get(i).getUserId());
                ps.setString(2, memberList.get(i).getUserName());
                ps.setString(3, memberList.get(i).getPhoneNumber());
                ps.setString(4, memberList.get(i).getPassword());
                ps.setLong(5, memberList.get(i).getAuthorityId());
                ps.setTimestamp(6, Timestamp.valueOf(memberList.get(i).getCreatedAt()));
            }

            @Override
            public int getBatchSize() {
                return subMemberList.size();
            }
        });

        return batchCount + 1;
    }


    /**
     * 데이터 초기화를 위한 랜덤 LocalDateTime 생성 메소드
     * @return
     */
    private LocalDateTime generateRandomDateTime() {
        long startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0)
                .toEpochSecond(ZoneOffset.UTC);
        long endDateTime = LocalDateTime.of(2023, Month.DECEMBER, 31, 23, 59)
                .toEpochSecond(ZoneOffset.UTC);

        long randomDateTime = ThreadLocalRandom.current().nextLong(startDateTime, endDateTime);

        return LocalDateTime.ofEpochSecond(randomDateTime, 0, ZoneOffset.UTC);
    }

}
