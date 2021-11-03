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

public class PersonPropertyTransformer extends AbstractPropertyTransformer<Person> {

    public PersonPropertyTransformer() {
        super(UserDto.class);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object transform(Object o) {
        if (!(o instanceof Person)) {
            if (o instanceof Collection) {
                Collection collection = (Collection) o;

                if (collection.isEmpty()) {
                    return o;
                }

                Object next = collection.iterator().next();

                if (!(next instanceof Person)) {
                    return o;
                }

                Collection<UserDto> userDtoCollection = newCollectionInstance(collection.getClass());
                collection.forEach(user -> userDtoCollection.add(buildUserDto((Person) user)));

                return userDtoCollection;
            }

            return o;
        }

        return buildUserDto((Person) o);
    }

    private UserDto buildUserDto(Person user) {
//        Person userCredentials = user.getPerson();

        UserDto.UserDtoBuilder builder = UserDto.builder().id(user.getUid()).code(user.getCode()).displayName(user.getDisplayName());

//        if (userCredentials != null) {
//            builder.username(userCredentials.getUsername());
//        }

        return builder.build();
    }

    public static class UserDto {

        private String id;

        private String code;

        private String name;

        private String displayName;

        private String username;

        public UserDto(UserDtoBuilder userDtoBuilder) {
            this.id = userDtoBuilder.id;
            this.code = userDtoBuilder.code;
            this.name = userDtoBuilder.name;
            this.displayName = userDtoBuilder.displayName;
            this.username = userDtoBuilder.username;
        }

        public static UserDtoBuilder builder() {
            return new UserDtoBuilder();
        }

        @JsonProperty
        public String getId() {
            return id;
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
        public String getUsername() {
            return username;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            UserDto userDto = (UserDto) o;

            if (id != null ? !id.equals(userDto.id) : userDto.id != null) return false;
            if (code != null ? !code.equals(userDto.code) : userDto.code != null) return false;
            if (name != null ? !name.equals(userDto.name) : userDto.name != null) return false;
            if (displayName != null ? !displayName.equals(userDto.displayName) : userDto.displayName != null) return false;
            return username != null ? username.equals(userDto.username) : userDto.username == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (code != null ? code.hashCode() : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
            result = 31 * result + (username != null ? username.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return (
                "UserDto{" +
                "id='" +
                id +
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
                ", username='" +
                username +
                '\'' +
                '}'
            );
        }

        public static class UserDtoBuilder {

            private String id;

            private String code;

            private String name;

            private String displayName;

            private String username;

            public UserDtoBuilder id(String id) {
                this.id = id;
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

            public UserDtoBuilder username(String username) {
                this.username = username;
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

    public static final class JacksonSerialize extends StdSerializer<Person> {

        public JacksonSerialize() {
            super(Person.class);
        }

        @Override
        public void serialize(Person user, JsonGenerator gen, SerializerProvider provider) throws IOException {
//            Person userCredentials = user.getPerson();

            gen.writeStartObject();
            gen.writeStringField("id", user.getUid());
            gen.writeStringField("code", user.getCode());
            gen.writeStringField("name", user.getName());
            gen.writeStringField("displayName", user.getDisplayName());

//            if (userCredentials != null) {
//                gen.writeStringField("username", userCredentials.getUsername());
//            }

            gen.writeEndObject();
        }
    }

    public static final class JacksonDeserialize extends StdDeserializer<Person> {

        public JacksonDeserialize() {
            super(Person.class);
        }

        @Override
        public Person deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Person user = new Person();
//            Person userCredentials = new Person();
//            user.setPerson(userCredentials);

            JsonNode node = jp.getCodec().readTree(jp);

            if (node.has("id")) {
                String identifier = node.get("id").asText();

                if (CodeGenerator.isValidUid(identifier)) {
                    user.setUid(identifier);
//                    userCredentials.setUid(identifier);
                } else {
//                    userCredentials.setUuid(UUID.fromString(identifier));
                }
            }

            if (node.has("code")) {
                String code = node.get("code").asText();

                user.setCode(code);
//                userCredentials.setCode(code);
            }

            if (node.has("username")) {
//                userCredentials.setLogin(node.get("login").asText());
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
