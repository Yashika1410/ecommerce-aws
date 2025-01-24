package com.example.ecommerce.entities;

import com.example.ecommerce.common.enums.DocType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "doc_mapping")
@Builder
public class DocumentMapping {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String url;
  @Enumerated(EnumType.STRING)
  private DocType type;
  private String fileType;
  private String uuid;
  @Column(columnDefinition = "text")
  private String description;
  private int position;
}
