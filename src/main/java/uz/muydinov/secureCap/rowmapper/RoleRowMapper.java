package uz.muydinov.secureCap.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import uz.muydinov.secureCap.domain.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));
        role.setPermission(resultSet.getString("permission"));
        return role;
    }
}
