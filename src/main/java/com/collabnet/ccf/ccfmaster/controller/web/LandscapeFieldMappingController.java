package com.collabnet.ccf.ccfmaster.controller.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mvc.extensions.flash.FlashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingExternalAppTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingScope;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMap;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingValueMapEntry;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.model.FieldMappingTemplateModel;



@RequestMapping("/admin/**")
@Controller
public class LandscapeFieldMappingController extends AbstractLandscapeController {

	
	
	private static final String LINK_TO_PROJECT_MAPPING = "Link to Project Mapping ";
	private static final String LINK_TO_CONNECTOR_MAPPING = "Link to Connector Mapping ";
	private static final String PROJECT_TEMPLATE_TYPE = "project";
	private static final String FIELD_MAPPING_ID = "fieldmappingid";
	private static final String RMD_ID_REQUEST_PARAM = "rmdid";
	private static final String CONNECTOR_TEMPLATE_TYPE = "connector";
	
	
	/**
	 * Controller method to display associated field mappings for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD, method = RequestMethod.GET)
	public String listForRepositoryMappingDirection(
			@RequestParam(RMD_ID_REQUEST_PARAM) RepositoryMappingDirection rmd,
			Model model) {
		getFieldMappingForRMD(rmd, model);
		return UIPathConstants.VIEW_ASSOCIATED_FIELD_MAPPINGS;

	}

	
	private List<FieldMapping> getFieldMappingForRMD(
			RepositoryMappingDirection rmd, Model model) {
		List<FieldMapping> fieldMapping=FieldMapping.findFieldMappingsByParent(rmd).getResultList();
		if( "FORWARD".equalsIgnoreCase(rmd.getDirection().name()))
		{
			model.addAttribute("rmdname", rmd.getRepositoryMapping().getTeamForgeRepositoryId()+"=>"+rmd.getRepositoryMapping().getParticipantRepositoryId());
		}
		else{
			model.addAttribute("rmdname", rmd.getRepositoryMapping().getParticipantRepositoryId()+"=>"+rmd.getRepositoryMapping().getTeamForgeRepositoryId());
		}
		model.addAttribute("rmdid", rmd.getId());
		model.addAttribute("direction", rmd.getDirection().name());
		model.addAttribute("activeFieldMappingid", rmd.getActiveFieldMapping().getId());
		populateFieldMappingModel(fieldMapping,model);
		return fieldMapping;
	}
	
	
	/**
	 * Controller method to create field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.CREATE_FIELD_MAPPING, method = RequestMethod.POST)
	public String createFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			Model model,HttpServletRequest request) {
		
		Landscape landscape=ControllerHelper.findLandscape();
		FieldMappingTemplateModel fieldMappingTemplateModel=new FieldMappingTemplateModel();
		//Get landscape templates by landscape and direction - connector templates
				List<FieldMappingLandscapeTemplate> fieldMappingLandscapeTemplate = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplatesByParentAndDirection(landscape, rmd.getDirection()).getResultList();
		//Get external app templates by external app and direction - project templates
				List<FieldMappingExternalAppTemplate> fieldMappingExternalAppTemplate = 
						FieldMappingExternalAppTemplate.findFieldMappingExternalAppTemplatesByParentAndDirection(rmd.getRepositoryMapping().getExternalApp(), rmd.getDirection()).getResultList();
				
		fieldMappingTemplateModel.setFieldMappingLandscapeTemplate(fieldMappingLandscapeTemplate);
		fieldMappingTemplateModel.setFieldMappingExternalAppTemplate(fieldMappingExternalAppTemplate);
		model.addAttribute("rmdid", rmd.getId());
		model.addAttribute("direction", rmd.getDirection().name());
		model.addAttribute("fieldMappingTemplateModel",fieldMappingTemplateModel);
		model.addAttribute("selectedLink", "repositorymappings");
		return  UIPathConstants.DISPALY_SAVE_FIELD_MAPPING;
	}
	

	/**
	 * Controller method to save new field mapping for the existing RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.SAVE_NEW_FIELD_MAPPING, method = RequestMethod.POST)
	public String saveNewFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			FieldMappingTemplateModel fieldMappingTemplateModel,Model model,HttpServletRequest request) {
		try{
				persistFieldMapping(fieldMappingTemplateModel, rmd);
			
		} catch (Exception exception) {
		FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_CREATE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute("rmdid", rmd.getId());
		return "redirect:/" + UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD;

	}

	
	/**
	 * Persists new field mapping for repository mapping direction
	 */
	private void persistFieldMapping(FieldMappingTemplateModel fieldMappingTemplateModel,RepositoryMappingDirection rmd) {
		// link a template true/false
		boolean linkToTemplate = fieldMappingTemplateModel.isLinktoTemplate();
		// template type connector/project
		FieldMapping fieldMapping = new FieldMapping();
		fieldMapping.setParent(rmd);
		// If link to template is selected
		if (linkToTemplate) {
			createLinkFieldMapping2Template(rmd, fieldMappingTemplateModel,fieldMapping);
		}
		else {
			// If link to field mapping template is not selected and if new field mapping name is given
			createFieldMappingTemplate(fieldMappingTemplateModel, rmd,fieldMapping);
		}
	}


	/**
	 * @param fieldMappingTemplateModel
	 * @param rmd
	 * @param fieldMappingLandscapeTemplate
	 * @param fieldMappingExternalAppTemplate
	 * @param fieldMapping
	 */
	private void createFieldMappingTemplate(
			FieldMappingTemplateModel fieldMappingTemplateModel,
			RepositoryMappingDirection rmd,	FieldMapping fieldMapping) {
		// Check if new field mapping with the same name already exists. display error message to the user
		if (isTemplateExists(fieldMappingTemplateModel.getFieldmappingName(), rmd,FieldMappingScope.REPOSITORY_MAPPING_DIRECTION)) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_ALREADY_EXISTS_MESSAGE);
		}
		else{
			fieldMapping.setName(fieldMappingTemplateModel.getFieldmappingName()); 
			fieldMapping.setScope(FieldMappingScope.REPOSITORY_MAPPING_DIRECTION);
			if (CONNECTOR_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType())) {
				FieldMappingLandscapeTemplate fieldMappingLandscapeTemplate=fieldMappingTemplateModel.getFieldMappingLandscapeTemplate().get(0);
				fieldMapping.setKind(fieldMappingLandscapeTemplate.getKind());
				fieldMapping.setRules(cloneFieldMappingRules(fieldMappingLandscapeTemplate.getRules()));
				fieldMapping.setValueMaps(cloneFieldMappingValueMap(fieldMappingLandscapeTemplate.getValueMaps()));
			}
			else if (PROJECT_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType())) {
				FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate=fieldMappingTemplateModel.getFieldMappingExternalAppTemplate().get(0);
				fieldMapping.setKind(fieldMappingExternalAppTemplate.getKind());
				fieldMapping.setRules(cloneFieldMappingRules(fieldMappingExternalAppTemplate.getRules()));
				fieldMapping.setValueMaps(cloneFieldMappingValueMap(fieldMappingExternalAppTemplate.getValueMaps()));
			}
			fieldMapping.persist();
			FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_CREATE_SUCCESS_MESSAGE);
		}
	}


	/**
	 * @param rmd
	 * @param fieldMappingLandscapeTemplate
	 * @param fieldMappingExternalAppTemplate
	 * @param templateType
	 * @param fieldMapping
	 */
	private void createLinkFieldMapping2Template(RepositoryMappingDirection rmd,
			FieldMappingTemplateModel fieldMappingTemplateModel, FieldMapping fieldMapping) {
		FieldMappingLandscapeTemplate fieldMappingLandscapeTemplate=fieldMappingTemplateModel.getFieldMappingLandscapeTemplate().get(0);
		FieldMappingExternalAppTemplate fieldMappingExternalAppTemplate=fieldMappingTemplateModel.getFieldMappingExternalAppTemplate().get(0);
		
		//  if its a connector template and the template is already with the same name display error message
		if (CONNECTOR_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType()) && isTemplateExists(LINK_TO_CONNECTOR_MAPPING+fieldMappingLandscapeTemplate.getName(), rmd,FieldMappingScope.LANDSCAPE)) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_ALREADY_LINKED_MESSAGE);
		}
		// if its a project template and the template is already with the same name display error message
		else if(PROJECT_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType()) && isTemplateExists(LINK_TO_PROJECT_MAPPING+fieldMappingExternalAppTemplate.getName(), rmd,FieldMappingScope.EXTERNAL_APP)) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_ALREADY_LINKED_MESSAGE);
		}
		else{
			if (CONNECTOR_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType())) {
				fieldMapping.setScope(FieldMappingScope.LANDSCAPE);
				fieldMapping.setName(LINK_TO_CONNECTOR_MAPPING+fieldMappingLandscapeTemplate.getName());
				fieldMapping.setKind(fieldMappingLandscapeTemplate.getKind());
			}
			else if(PROJECT_TEMPLATE_TYPE.equals(fieldMappingTemplateModel.getTemplateType())){
				fieldMapping.setScope(FieldMappingScope.EXTERNAL_APP);
				fieldMapping.setName(LINK_TO_PROJECT_MAPPING+fieldMappingExternalAppTemplate.getName());
				fieldMapping.setKind(fieldMappingExternalAppTemplate.getKind());
			}
			fieldMapping.setRules(null);
			fieldMapping.setValueMaps(null);
			fieldMapping.persist();
			FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_CREATE_SUCCESS_MESSAGE);
		}
	}
		
	
	
	private List<FieldMappingRule> cloneFieldMappingRules(List<FieldMappingRule> templateRules){
		List<FieldMappingRule> newFieldMappingRule = new ArrayList<FieldMappingRule>();
		for(FieldMappingRule templateRule:templateRules){
			FieldMappingRule newRule = new FieldMappingRule();
			newRule.setCondition(templateRule.getCondition());
			newRule.setDescription(templateRule.getDescription());
			newRule.setName(templateRule.getName());
			newRule.setSource(templateRule.getSource());
			newRule.setSourceIsTopLevelAttribute(templateRule.isSourceIsTopLevelAttribute());
			newRule.setTarget(templateRule.getTarget());
			newRule.setTargetIsTopLevelAttribute(templateRule.isTargetIsTopLevelAttribute());
			newRule.setType(templateRule.getType());
			newRule.setValueMapName(templateRule.getValueMapName());
			newRule.setXmlContent(templateRule.getXmlContent());
			newFieldMappingRule.add(newRule);
		}
		return newFieldMappingRule;
	}
	
	private List<FieldMappingValueMap> cloneFieldMappingValueMap(List<FieldMappingValueMap> templateValueMaps){
		List<FieldMappingValueMap> newFieldMappingValueMap = new ArrayList<FieldMappingValueMap>();
		for(FieldMappingValueMap templateMap:templateValueMaps){
			FieldMappingValueMap newValueMap = new FieldMappingValueMap();
			newValueMap.setName(templateMap.getName());
			newValueMap.setDefaultValue(templateMap.getDefaultValue());
			newValueMap.setHasDefault(templateMap.isHasDefault());
			newValueMap.setEntries(cloneFieldMappingValueMapEntry(templateMap.getEntries()));			
			newFieldMappingValueMap.add(newValueMap);
		}
		return newFieldMappingValueMap;
	}
	
	private List<FieldMappingValueMapEntry> cloneFieldMappingValueMapEntry(List<FieldMappingValueMapEntry> templateValueMapEntries){
		List<FieldMappingValueMapEntry> newFieldMappingValueMapEntries = new ArrayList<FieldMappingValueMapEntry>();
		for(FieldMappingValueMapEntry templateMapEntry:templateValueMapEntries){
			FieldMappingValueMapEntry newValueMap = new FieldMappingValueMapEntry();
			newValueMap.setSource(templateMapEntry.getSource());
			newValueMap.setTarget(templateMapEntry.getTarget());			
			newFieldMappingValueMapEntries.add(newValueMap);
		}
		return newFieldMappingValueMapEntries;
	}

	
	/**
	 * Controller method to set active field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.SETAS_ACTIVE_FIELD_MAPPING, method = RequestMethod.POST)
	public String setasActiveFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			@RequestParam(FIELD_MAPPING_ID) FieldMapping fieldMapping,
			Model model,HttpServletRequest request) {
		try{
			rmd.setActiveFieldMapping(fieldMapping);
			rmd.merge();
			FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_SUCCESS_MESSAGE);
		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_SETAS_ACTIVE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute("rmdid", rmd.getId());
		return "redirect:/" + UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD;

	}
	
	/**
	 * Controller method to set delete the field mapping for the RMD
	 * 
	 */ 
	@RequestMapping(value = "/"+UIPathConstants.DELETE_ASSOCIATED_FIELD_MAPPINGS, method = RequestMethod.POST)
	public String deleteFieldMapping(
			@RequestParam(RMD_ID_REQUEST_PARAM)  RepositoryMappingDirection rmd,
			@RequestParam(FIELD_MAPPING_ID) String[] items,
			Model model,HttpServletRequest request) {
		if (items == null) 
			items = new String[0];
		try {
			for (String fieldMappingId: items ) {
				if(rmd.getActiveFieldMapping().getId() == Long.valueOf(fieldMappingId).longValue()){
					FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_NODELETE_ACTIVE_FAILURE_MESSAGE);
				}
				else{
					FieldMapping fieldMappingEntry=FieldMapping.findFieldMapping(new Long(fieldMappingId));
					fieldMappingEntry.remove();
					FlashMap.setSuccessMessage(ControllerConstants.FIELD_MAPPING_DELETE_SUCCESS_MESSAGE);
				}
			}
			
		} catch (Exception exception) {
			FlashMap.setErrorMessage(ControllerConstants.FIELD_MAPPING_DELETE_FAILURE_MESSAGE, exception.getMessage());
		}
		model.asMap().clear();
		model.addAttribute(RMD_ID_REQUEST_PARAM, rmd.getId());
		return "redirect:/" + UIPathConstants.ASSOCIATED_FIELD_MAPPINGS_BY_RMD;

	}
	
	void populateFieldMappingModel(List<FieldMapping> fieldMappingEntrys,  Model model) {
		Landscape landscape=ControllerHelper.findLandscape();
		model.addAttribute("participant",landscape.getParticipant());
		model.addAttribute("landscape",landscape);
		model.addAttribute("selectedLink", "repositorymappings");
		model.addAttribute("associatedfmmodel", fieldMappingEntrys);
	}

	private boolean isTemplateExists(String templateName, RepositoryMappingDirection rmd,FieldMappingScope scope) {
		boolean templateexists = false;
		if(FieldMapping.findFieldMappingsByNameAndParentAndScope(templateName, rmd, scope).getResultList().size()!=0){
			templateexists = true;
		}
		else{
			templateexists = false;
		}
		return templateexists;
	}
	
	
}
