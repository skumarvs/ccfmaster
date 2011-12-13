// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FieldMappingValueMap_Roo_Entity {
    
    declare @type: FieldMappingValueMap: @Entity;
    
    @PersistenceContext
    transient EntityManager FieldMappingValueMap.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long FieldMappingValueMap.id;
    
    @Version
    @Column(name = "version")
    private Integer FieldMappingValueMap.version;
    
    public Long FieldMappingValueMap.getId() {
        return this.id;
    }
    
    public void FieldMappingValueMap.setId(Long id) {
        this.id = id;
    }
    
    public Integer FieldMappingValueMap.getVersion() {
        return this.version;
    }
    
    public void FieldMappingValueMap.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void FieldMappingValueMap.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void FieldMappingValueMap.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FieldMappingValueMap attached = FieldMappingValueMap.findFieldMappingValueMap(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void FieldMappingValueMap.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void FieldMappingValueMap.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public FieldMappingValueMap FieldMappingValueMap.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FieldMappingValueMap merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager FieldMappingValueMap.entityManager() {
        EntityManager em = new FieldMappingValueMap().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long FieldMappingValueMap.countFieldMappingValueMaps() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FieldMappingValueMap o", Long.class).getSingleResult();
    }
    
    public static List<FieldMappingValueMap> FieldMappingValueMap.findAllFieldMappingValueMaps() {
        return entityManager().createQuery("SELECT o FROM FieldMappingValueMap o", FieldMappingValueMap.class).getResultList();
    }
    
    public static FieldMappingValueMap FieldMappingValueMap.findFieldMappingValueMap(Long id) {
        if (id == null) return null;
        return entityManager().find(FieldMappingValueMap.class, id);
    }
    
    public static List<FieldMappingValueMap> FieldMappingValueMap.findFieldMappingValueMapEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FieldMappingValueMap o", FieldMappingValueMap.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
