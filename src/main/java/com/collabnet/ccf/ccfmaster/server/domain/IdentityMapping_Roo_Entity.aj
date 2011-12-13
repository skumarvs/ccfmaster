// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.IdentityMapping;
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

privileged aspect IdentityMapping_Roo_Entity {
    
    declare @type: IdentityMapping: @Entity;
    
    @PersistenceContext
    transient EntityManager IdentityMapping.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long IdentityMapping.id;
    
    @Version
    @Column(name = "version")
    private Integer IdentityMapping.version;
    
    public Long IdentityMapping.getId() {
        return this.id;
    }
    
    public void IdentityMapping.setId(Long id) {
        this.id = id;
    }
    
    public Integer IdentityMapping.getVersion() {
        return this.version;
    }
    
    public void IdentityMapping.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void IdentityMapping.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void IdentityMapping.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            IdentityMapping attached = IdentityMapping.findIdentityMapping(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void IdentityMapping.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void IdentityMapping.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public IdentityMapping IdentityMapping.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        IdentityMapping merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager IdentityMapping.entityManager() {
        EntityManager em = new IdentityMapping().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long IdentityMapping.countIdentityMappings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM IdentityMapping o", Long.class).getSingleResult();
    }
    
    public static List<IdentityMapping> IdentityMapping.findAllIdentityMappings() {
        return entityManager().createQuery("SELECT o FROM IdentityMapping o", IdentityMapping.class).getResultList();
    }
    
    public static IdentityMapping IdentityMapping.findIdentityMapping(Long id) {
        if (id == null) return null;
        return entityManager().find(IdentityMapping.class, id);
    }
    
    public static List<IdentityMapping> IdentityMapping.findIdentityMappingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM IdentityMapping o", IdentityMapping.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
