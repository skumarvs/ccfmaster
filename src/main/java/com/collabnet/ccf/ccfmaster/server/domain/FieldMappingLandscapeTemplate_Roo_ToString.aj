// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import java.lang.String;

privileged aspect FieldMappingLandscapeTemplate_Roo_ToString {
    
    public String FieldMappingLandscapeTemplate.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Direction: ").append(getDirection()).append(", ");
        sb.append("Kind: ").append(getKind()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Parent: ").append(getParent()).append(", ");
        sb.append("Rules: ").append(getRules() == null ? "null" : getRules().size()).append(", ");
        sb.append("ValueMaps: ").append(getValueMaps() == null ? "null" : getValueMaps().size());
        return sb.toString();
    }
    
}
