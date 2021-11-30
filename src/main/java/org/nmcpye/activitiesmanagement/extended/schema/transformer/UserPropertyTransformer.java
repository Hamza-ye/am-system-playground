package org.nmcpye.activitiesmanagement.extended.schema.transformer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.nmcpye.activitiesmanagement.domain.User;
import org.nmcpye.activitiesmanagement.domain.person.Person;
import org.nmcpye.activitiesmanagement.extended.common.CodeGenerator;
import org.nmcpye.activitiesmanagement.extended.schema.AbstractPropertyTransformer;

import java.io.IOException;
import java.util.*;

public class UserPropertyTransformer extends AbstractPropertyTransformer<User> {

    public UserPropertyTransformer() {
        super(UserDto.class);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object transform(Object o) {
        if (!(o instanceof User)) {
            if (o instanceof Collection) {
                Collection collection = (Collection) o;

                if (collection.isEmpty()) {
                    return o;
                }

                Object next = collection.iterator().next();

                if (!(next instanceof User)) {
                    return o;
                }

                Collection<UserDto> userDtoCollection = newCollectionInstance(collection.getClass());
                collection.forEach(user -> userDtoCollection.add(buildUserDto((User) user)));

                return userDtoCollection;
            }

            return o;
        }

        return buildUserDto((User) o);
    }

    private UserDto buildUserDto(User user) {
        Person userCredentials = user.getPerson();

        UserDto.UserDtoBuilder builder = UserDto.builder().id(user.getId()).uid(user.getUid()).code(user.getCode()).displayName(user.getDisplayName());

        if (userCredentials != null) {
            builder.login(userCredentials.getUsername());
        }

        return builder.build();
    }

    public static class UserDto {

        private Long id;

        private String uid;

        private String code;

        private String name;

        private String displayName;

        private String login;

        public UserDto(UserDtoBuilder userDtoBuilder) {
            this.id = userDtoBuilder.id;
            this.uid = userDtoBuilder.uid;
            this.code = userDtoBuilder.code;
            this.name = userDtoBuilder.name;
            this.displayName = userDtoBuilder.displayName;
            this.login = userDtoBuilder.login;
        }

        public static UserDtoBuilder builder() {
            return new UserDtoBuilder();
        }

        @JsonProperty
        public Long getId() {
            return id;
        }

        @JsonProperty
        public String getUid() {
            return uid;
        }

        @JsonProperty
        public String getCode() {
            return code;
        }

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public String getDisplayName() {
            return displayName;
        }

        @JsonProperty
        public String getLogin() {
            return login;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserDto userDto = (UserDto) o;

            if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
            if (uid != null ? !uid.equals(userDto.uid) : userDto.uid != null) return false;
            if (code != null ? !code.equals(userDto.code) : userDto.code != null) return false;
            if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
            if (displayName != null ? !displayName.equals(userDto.displayName) : userDto.displayName != null)
                return false;
            return login != null ? login.equals(userDto.login) : userDto.login == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (uid != null ? uid.hashCode() : 0);
            result = 31 * result + (code != null ? code.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
            result = 31 * result + (login != null ? login.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return (
                "UserDto{" +
                    "id='" +
                    id +
                    '\'' +
                    "uid='" +
                    uid +
                    '\'' +
                    ", code='" +
                    code +
                    '\'' +
                    ", name='" +
                    name +
                    '\'' +
                    ", displayName='" +
                    displayName +
                    '\'' +
                    ", login='" +
                    login +
                    '\'' +
                    '}'
            );
        }

        public static class UserDtoBuilder {

            private Long id;
            private String uid;

            private String code;

            private String name;

            private String displayName;

            private String login;

            public UserDtoBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public UserDtoBuilder uid(String uid) {
                this.uid = uid;
                return this;
            }

            public UserDtoBuilder code(String code) {
                this.code = code;
                return this;
            }

            public UserDtoBuilder name(String name) {
                this.name = name;
                return this;
            }

            public UserDtoBuilder displayName(String displayName) {
                this.displayName = displayName;
                return this;
            }

            public UserDtoBuilder login(String username) {
                this.login = username;
                return this;
            }

            //Return the finally consrcuted User object
            public UserDto build() {
                UserDto user = new UserDto(this);
                validateUserObject(user);
                return user;
            }

            private void validateUserObject(UserDto user) {
                //Do some basic validations to check
                //if user object does not break any assumption of system
            }
        }
    }

    public static final class JacksonSerialize extends StdSerializer<User> {

        public JacksonSerialize() {
            super(User.class);
        }

        @Override
        public void serialize(User user, JsonGenerator gen, SerializerProvider provider) throws IOException {
            Person userCredentials = user.getPerson();

            gen.writeStartObject();
            gen.writeNumberField("id", user.getId());
            gen.writeStringField("uid", user.getUid());
            gen.writeStringField("code", user.getCode());
            gen.writeStringField("name", user.getName());
            gen.writeStringField("displayName", user.getDisplayName());
            gen.writeStringField("login", user.getLogin());
//            gen.writeObject(user.getAuthorities());

//            if (userCredentials != null) {
//                gen.writeStringField("login", userCredentials.getLogin());
//            }

            gen.writeEndObject();
        }
    }

    public static final class JacksonDeserialize extends StdDeserializer<User> {

        public JacksonDeserialize() {
            super(User.class);
        }

        @Override
        public User deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            User user = new User();
            Person person = new Person();
            user.setPerson(person);

            JsonNode node = jp.getCodec().readTree(jp);

//            if (node.has("id")) {
//                String identifier = node.get("id").asText();
            if (node.has("uid")) {
                String identifier = node.get("uid").asText();

                if (CodeGenerator.isValidUid(identifier)) {
                    user.setUid(identifier);
                    person.setUid(identifier);
                } else {
                    person.setUuid(UUID.fromString(identifier));
                }
            }

            // Extend
            if (node.has("id")) {
                Long id = node.get("id").asLong();

                user.setId(id);
                person.setId(id);
            }

            if (node.has("code")) {
                String code = node.get("code").asText();

                user.setCode(code);
                person.setCode(code);
            }

            if (node.has("login")) {
                person.setLogin(node.get("login").asText());
            }

            return user;
        }
    }

    private static <E> Collection<E> newCollectionInstance(Class<?> clazz) {
        if (List.class.isAssignableFrom(clazz)) {
            return new ArrayList<>();
        } else if (Set.class.isAssignableFrom(clazz)) {
            return new HashSet<>();
        } else {
            throw new RuntimeException("Unknown Collection type.");
        }
    }
}
