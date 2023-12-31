package com.example.It_info_pass_master.DAO;

import com.example.It_info_pass_master.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PgAccountDao implements AccountDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * IDとPASSからUserを探すメソッド
     *
     * @param idPassRecord
     * @return
     */
    @Override
    public UserRecord findUser(IdPassRecord idPassRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", idPassRecord.loginId());
        param.addValue("pass", idPassRecord.password());
        var list = jdbcTemplate.query("SELECT * FROM users WHERE login_id = :id AND password = :pass",
                param, new DataClassRowMapper<>(UserRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * IDが重複しないかチェックするメソッド
     *
     * @param id
     * @return
     */
    @Override
    public UserRecord checkIdExist(String id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("loginId", id);
        var list = jdbcTemplate.query("SELECT * FROM users WHERE login_id = :loginId",
                param, new DataClassRowMapper<>(UserRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * userの新規追加
     *
     * @param
     * @return
     */
    @Override
    public int insertUser(UserRecord userRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("loginId", userRecord.loginId());
        param.addValue("password", userRecord.password());
        param.addValue("name", userRecord.name());
        param.addValue("role", userRecord.role());
        int count = jdbcTemplate.update("INSERT INTO Users" +
                "(login_id, password, name, role)" +
                " VALUES (:loginId, :password, :name, :role)", param);
        return count == 1 ? count : null;
    }


    /**
     * ゲームスコアの上位３位の取得
     *
     * @return
     */
    @Override
    public List<RankingRecord> findTopThree(String age) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("ageNum", age);

        return jdbcTemplate.query(
                "SELECT u.name, MIN(ug.game_score) AS game_score " +
                        "FROM users u " +
                        "JOIN user_game ug ON u.id = ug.user_id " +
                        "JOIN age a ON ug.age_id = a.id " +
                        "WHERE a.age = :ageNum " +
                        "GROUP BY u.name " +
                        "ORDER BY game_score ASC " +
                        "LIMIT 3",
                param,
                new DataClassRowMapper<>(RankingRecord.class)
        );
    }


    /**
     * 年代の全取得
     *
     * @return
     */
    @Override
    public List<UserAgeRecord> findAgeAll() {
        return jdbcTemplate.query("SELECT id, age FROM age ORDER BY age ASC",
                new DataClassRowMapper<>(UserAgeRecord.class));
    }

    /**
     * ユーザーのランキング取得
     *
     * @return
     */
    public MyRankRecord findUserRank(String age, int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("ageNum", age);
        param.addValue("idNum", id);
        var list = jdbcTemplate.query(
                "SELECT rank, game_score " +
                        "FROM (" +
                        "    SELECT t.user_id, t.game_score, " +
                        "           DENSE_RANK() OVER (ORDER BY t.game_score ASC) AS rank " +
                        "    FROM (" +
                        "        SELECT ug.user_id, MIN(ug.game_score) AS game_score " +
                        "        FROM user_game ug " +
                        "        WHERE ug.age_id = (SELECT id FROM age WHERE age = :ageNum)" +
                        "        GROUP BY ug.user_id" +
                        "    ) AS t" +
                        ") AS s " +
                        "WHERE s.user_id = :idNum",
                param, new DataClassRowMapper<>(MyRankRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * userのnameのアップデート
     *
     * @param userRecord
     * @return
     */
    @Override
    public int userNameUpdate(UserRecord userRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("name", userRecord.name());
        param.addValue("id", userRecord.id());
        int count = jdbcTemplate.update("UPDATE users SET name = :name WHERE id = :id", param);
        return count == 1 ? count : null;
    }

    /**
     * userのパスワードのアップデート
     *
     * @param userRecord
     * @return
     */
    @Override
    public int userPassUpdate(UserRecord userRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("pass", userRecord.password());
        param.addValue("id", userRecord.id());
        int count = jdbcTemplate.update("UPDATE users SET password = :pass WHERE id = :id", param);
        return count == 1 ? count : null;
    }

    /**
     * userの削除
     *
     * @param userRecord
     * @return
     */
    @Override
    public int userDelete(UserRecord userRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", userRecord.id());
        int count = jdbcTemplate.update("DELETE FROM users WHERE id = :id", param);
        return count == 1 ? count : null;
    }

    /**
     * userテーブルの全取得
     *
     * @return
     */
    @Override
    public List<UserRecord> findAllUser() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id",
                new DataClassRowMapper<>(UserRecord.class));
    }

    /**
     * Inquiryテーブルの追加
     *
     * @param inquiryRecord
     * @return
     */
    @Override
    public int insertInquiry(InquiryRecord inquiryRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("inquiryTitle", inquiryRecord.inquiryTitle());
        param.addValue("inquiryText", inquiryRecord.inquiryText());
        param.addValue("userId", inquiryRecord.userId());
        param.addValue("checkInquiry", inquiryRecord.checkInquiry());
        param.addValue("readInquiry", inquiryRecord.readInquiry());

        int count = jdbcTemplate.update("INSERT INTO inquiry" +
                "(inquiry_title, inquiry_text, user_id, check_inquiry, read_inquiry)" +
                " VALUES (:inquiryTitle, :inquiryText, :userId, :checkInquiry, :readInquiry)", param);

        return count == 1 ? count : null;
    }

    /**
     * Inquiryテーブルユーザー別全取得
     * @return
     */
    @Override
    public List<InquiryRecord> findAllInquiry(int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("userId", id);
        return jdbcTemplate.query("SELECT * FROM inquiry WHERE user_id = :userId ORDER BY check_inquiry ASC",param,
                new DataClassRowMapper<>(InquiryRecord.class));
    }

    /**
     * 問い合わせ詳細取得
     *
     * @param id
     * @return
     */
    @Override
    public InquiryRecord inquiryFindById(int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);
        var list = jdbcTemplate.query("SELECT * FROM inquiry WHERE id = :id",
                param, new DataClassRowMapper<>(InquiryRecord.class));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Inquiryテーブルユーザー別全取得
     * @return
     */
    @Override
    public List<InquiryAdminRecord> findAllInquiry() {
        return jdbcTemplate.query("SELECT i.id, u.name, i.inquiry_title, i.check_inquiry FROM inquiry i JOIN users u ON i.user_id = u.id ORDER BY i.check_inquiry ASC",
                new DataClassRowMapper<>(InquiryAdminRecord.class));
    }

    /**
     * Inquiryテーブルの解答を更新
     * @param inquiryRecord
     * @return
     */
    @Override
    public int updateAnswerInquiry(InquiryRecord inquiryRecord) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", inquiryRecord.id());
        param.addValue("inquiryAnswer", inquiryRecord.inquiryAnswer());
        param.addValue("checkInquiry", inquiryRecord.checkInquiry());
        param.addValue("readInquiry", inquiryRecord.readInquiry());

        int count = jdbcTemplate.update("UPDATE inquiry SET inquiry_answer = :inquiryAnswer, check_inquiry = :checkInquiry WHERE id = :id", param);
        return count == 1 ? count : null;
    }

    /**
     * Inquiryテーブルの既読を更新
     * @param
     * @return
     */
    @Override
    public int updateReadInquiry(int id) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("id", id);

        int count = jdbcTemplate.update("UPDATE inquiry SET read_inquiry = 1 WHERE id = :id", param);
        return count == 1 ? count : null;
    }

    /**
     * グラフ用データ取得
     * @param age
     * @param id
     * @return
     */
    @Override
    public List<GraphRecord> graphFindByAge(String age, int id) {
        String sql = "SELECT c.category_name, ROUND((COUNT(uc.perfect_check) * 100.0) / COUNT(q.id)) AS percentage " +
                "FROM category c " +
                "JOIN questions q ON c.id = q.category_id " +
                "JOIN question_age qa ON q.id = qa.question_id " +
                "JOIN age a ON qa.age_id = a.id " +
                "LEFT JOIN user_check uc ON qa.id = uc.question_age_id AND uc.user_id = :id " +
                "WHERE a.age = :age " +
                "GROUP BY c.category_name";

        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("age", age);
        param.addValue("id", id);

        return jdbcTemplate.query(sql, param, new DataClassRowMapper<>(GraphRecord.class));
    }


}
