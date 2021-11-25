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
        super(PersonDto.class);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
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

                Collection<PersonDto> personDtoCollection = newCollectionInstance(collection.getClass());
                collection.forEach(user -> personDtoCollection.add(buildUserDto((Person) user)));

                return personDtoCollection;
            }

            return o;
        }

        return buildUserDto((Person) o);
    }

    private PersonDto buildUserDto(Person person) {
        User userInfo = person.getUserInfo();

        PersonDto.PersonDtoBuilder builder = PersonDto.builder().uid(person.getUid()).code(person.getCode()).displayName(person.getDisplayName());

        if (userInfo != null) {
            builder.login(userInfo.getLogin());
        }

        return builder.build();
    }

    public static class PersonDto {

        private Long id;

        private String uid;

        private String code;

        private String name;

        private String displayName;

        private String login;

        public PersonDto(PersonDtoBuilder personDtoBuilder) {

            this.id = personDtoBuilder.id;
            this.uid = personDtoBuilder.uid;
            this.code = personDtoBuilder.code;
            this.name = personDtoBuilder.name;
            this.displayName = personDtoBuilder.displayName;
            this.login = personDtoBuilder.login;
        }

        public static PersonDtoBuilder builder() {
            return new PersonDtoBuilder();
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

            PersonDto personDto = (PersonDto) o;

            if (id != null ? !id.equals(personDto.id) : personDto.id != null) return false;
            if (uid != null ? !uid.equals(personDto.uid) : personDto.uid != null) return false;
            if (code != null ? !code.equals(personDto.code) : personDto.code != null) return false;
            if (name != null ? !name.equals(personDto.name) : personDto.name != null) return false;
            if (displayName != null ? !displayName.equals(personDto.displayName) : personDto.displayName != null)
                return false;
            return login != null ? login.equals(personDto.login) : personDto.login == null;
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
                    ", username='" +
                    login +
                    '\'' +
                    '}'
            );
        }

        public static class PersonDtoBuilder {

            private Long id;

            private String uid;

            private String code;

            private String name;

            private String displayName;

            private String login;

            public PersonDtoBuilder id(Long id) {
                this.id = id;
                return this;
            }

            public PersonDtoBuilder uid(String uid) {
                this.uid = uid;
                return this;
            }

            public PersonDtoBuilder code(String code) {
                this.code = code;
                return this;
            }

            public PersonDtoBuilder name(String name) {
                this.name = name;
                return this;
            }

            public PersonDtoBuilder displayName(String displayName) {
                this.displayName = displayName;
                return this;
            }

            public PersonDtoBuilder login(String username) {
                this.login = username;
                return this;
            }

            //Return the finally consrcuted User object
            public PersonDto build() {
                PersonDto user = new PersonDto(this);
                validateUserObject(user);
                return user;
            }

            private void validateUserObject(PersonDto user) {
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
            User userInfo = user.getUserInfo();

            gen.writeStartObject();
            gen.writeNumberField("id", user.getId());
            gen.writeStringField("uid", user.getUid());
            gen.writeStringField("code", user.getCode());
            gen.writeStringField("name", user.getName());
            gen.writeStringField("displayName", user.getDisplayName());

            if (userInfo != null) {
                gen.writeStringField("login", userInfo.getLogin());
            }

            gen.writeEndObject();
        }
    }

    public static final class JacksonDeserialize extends StdDeserializer<Person> {

        public JacksonDeserialize() {
            super(Person.class);
        }

        @Override
        public Person deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            Person person = new Person();
            User user1 = new User();
            person.setUserInfo(user1);

            JsonNode node = jp.getCodec().readTree(jp);

            if (node.has("uid")) {
                String identifier = node.get("uid").asText();

                if (CodeGenerator.isValidUid(identifier)) {
                    person.setUid(identifier);
                    user1.setUid(identifier);
                } else {
//                    user1.setU(UUID.fromString(identifier));
                }
            }

            if (node.has("id")) {
                Long id = node.get("id").asLong();

                person.setId(id);
                user1.setId(id);
            }

            if (node.has("code")) {
                String code = node.get("code").asText();

                person.setCode(code);
                user1.setCode(code);
            }

            if (node.has("login")) {
                user1.setLogin(node.get("login").asText());
            }

            return person;
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
