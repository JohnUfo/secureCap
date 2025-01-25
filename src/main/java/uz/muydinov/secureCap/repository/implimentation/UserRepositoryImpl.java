package uz.muydinov.secureCap.repository.implimentation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.muydinov.secureCap.domain.Role;
import uz.muydinov.secureCap.domain.User;
import uz.muydinov.secureCap.exception.ApiException;
import uz.muydinov.secureCap.repository.RoleRepository;
import uz.muydinov.secureCap.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static uz.muydinov.secureCap.enumeration.RoleType.ROLE_USER;
import static uz.muydinov.secureCap.enumeration.VerificationType.ACCOUNT;
import static uz.muydinov.secureCap.query.UserQuery.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbc;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        if (getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("Email already in use. Please use a different email and try again.");
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbc.update(INSERT_USER_QUERY, parameters, holder);

            // Retrieve the generated ID
            Map<String, Object> keys = holder.getKeys();
            if (keys != null) {
                user.setId(((Number) keys.get("id")).longValue());
            }

            // Add role to the user and set additional properties
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            jdbc.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));
            // emailService.sendVerificationUrl(user.getFirstName(),user.getEmail(),verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNonLocked(true);
            return user;
        } catch (Exception exception) {
            throw new ApiException("An error occurred. Please try again.");
        }
    }

    @Override
    public Collection list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public User update(User data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword())
                );
    }


    private Integer getEmailCount(String email) {
        return jdbc.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify" + type + "/" + key).toUriString();
    }
}
