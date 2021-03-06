// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.LandscapeConfig;
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

privileged aspect LandscapeConfig_Roo_Entity {
    
    declare @type: LandscapeConfig: @Entity;
    
    @PersistenceContext
    transient EntityManager LandscapeConfig.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long LandscapeConfig.id;
    
    @Version
    @Column(name = "version")
    private Integer LandscapeConfig.version;
    
    public Long LandscapeConfig.getId() {
        return this.id;
    }
    
    public void LandscapeConfig.setId(Long id) {
        this.id = id;
    }
    
    public Integer LandscapeConfig.getVersion() {
        return this.version;
    }
    
    public void LandscapeConfig.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void LandscapeConfig.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void LandscapeConfig.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LandscapeConfig attached = LandscapeConfig.findLandscapeConfig(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void LandscapeConfig.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void LandscapeConfig.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public LandscapeConfig LandscapeConfig.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LandscapeConfig merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager LandscapeConfig.entityManager() {
        EntityManager em = new LandscapeConfig().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long LandscapeConfig.countLandscapeConfigs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LandscapeConfig o", Long.class).getSingleResult();
    }
    
    public static List<LandscapeConfig> LandscapeConfig.findAllLandscapeConfigs() {
        return entityManager().createQuery("SELECT o FROM LandscapeConfig o", LandscapeConfig.class).getResultList();
    }
    
    public static LandscapeConfig LandscapeConfig.findLandscapeConfig(Long id) {
        if (id == null) return null;
        return entityManager().find(LandscapeConfig.class, id);
    }
    
    public static List<LandscapeConfig> LandscapeConfig.findLandscapeConfigEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LandscapeConfig o", LandscapeConfig.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
