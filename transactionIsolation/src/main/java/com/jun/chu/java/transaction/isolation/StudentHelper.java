package com.jun.chu.java.transaction.isolation;

import com.google.common.collect.Lists;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author chujun
 * @date 2022/3/5
 */
@UtilityClass
public class StudentHelper {
    public static List<Student> fetchStudents(final ResultSet rs) throws SQLException {
        List<Student> result = Lists.newArrayList();
        while (rs.next()) {
            long id = rs.getLong(1); // 注意：索引从1开始
            long grade = rs.getLong(2);
            String name = rs.getString(3);
            int gender = rs.getInt(4);
            result.add(Student.builder()
                .id(id).grade(grade).name(name).gender(gender)
                .build());
        }
        return result;
    }
}
