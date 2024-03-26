package com.project.course.managment.models.database;

import com.project.course.managment.constants.CommonsErrorConstants;
import com.project.course.managment.constants.CustomExceptionConstants;
import com.project.course.managment.exceptions.DocumentNotOwnedException;
import com.project.course.managment.exceptions.DocumentsAlreadyOwnedException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    private String name;

    @Column(nullable = false)
    @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    private String category;

    @Column(nullable = false)
    @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    private String description;

    @Column(nullable = false)
    @NotBlank(message = CommonsErrorConstants.REQUIRED_PARAM_ERROR_MESSAGE)
    private String link;

    @Setter(AccessLevel.NONE)
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Document> documents = new HashSet<>();


    public Set<Document> getDocuments() {
        return Collections.unmodifiableSet(documents);
    }

    public void addDocument(Document document) throws DocumentsAlreadyOwnedException {
        /*if (documents.contains(document)) {
            throw new DocumentsAlreadyOwnedException(
                    CustomExceptionConstants.DOCUMENT_ALREADY_OWNED_ERROR_CODE,
                    String.format(CustomExceptionConstants.DOCUMENT_ALREADY_OWNED_ERROR_MESSAGE,
                            document.getName()));
        }*/
        documents.add(document);
    }

    public void removeDocument(Document document) throws DocumentNotOwnedException {
        if (!documents.contains(document)) {
            throw new DocumentNotOwnedException(
                    CustomExceptionConstants.DOCUMENT_NOT_OWNED_ERROR_CODE,
                    CustomExceptionConstants.DOCUMENT_NOT_OWNED_ERROR_MESSAGE);
        }
        documents.remove(document);
    }
}
