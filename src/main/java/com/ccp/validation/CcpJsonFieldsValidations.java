package com.ccp.validation;

import java.util.Map;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.validation.annotations.AllowedValues;
import com.ccp.validation.annotations.ArrayNumbers;
import com.ccp.validation.annotations.ArraySize;
import com.ccp.validation.annotations.ArrayTextSize;
import com.ccp.validation.annotations.Day;
import com.ccp.validation.annotations.ObjectNumbers;
import com.ccp.validation.annotations.ObjectTextSize;
import com.ccp.validation.annotations.Regex;
import com.ccp.validation.annotations.SimpleObject;
import com.ccp.validation.annotations.ValidationRules;
import com.ccp.validation.annotations.Year;
import com.ccp.validation.enums.AllowedValuesValidations;
import com.ccp.validation.enums.BoundValidations;
import com.ccp.validation.enums.SimpleObjectValidations;

public class CcpJsonFieldsValidations {
	
	public static void validate(Class<?> clazz, Map<String, Object> map, String featureName) {
		
		boolean isNotPresent = clazz.isAnnotationPresent(ValidationRules.class) == false;
		
		if(isNotPresent) {
			return;
		}
		
		ValidationRules rules = clazz.getAnnotation(ValidationRules.class);
		
		validate(rules, map, featureName);
	}
	
	public static void validate(ValidationRules rules, Map<String, Object> map, String featureName) {
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(map);

		Class<?> rulesClass = rules.rulesClass();
		
		rules = rulesClass.isAnnotationPresent(ValidationRules.class) ? rulesClass.getAnnotation(ValidationRules.class) : rules;
		
		CcpJsonRepresentation evidences = CcpConstants.EMPTY_JSON;

		evidences = validateBounds(rules, json, evidences);

		evidences = validateRestricted(rules, json, evidences);

		evidences = simpleValidation(rules, json, evidences);

		CcpJsonRepresentation errors = evidences.getInnerJson("errors");
		
		boolean noErrors = errors.isEmpty();

		if (noErrors) {
			return;
		}
		
		CcpJsonRepresentation specification = getSpecification(featureName, rules);
		
		CcpJsonRepresentation result = evidences.put("specification", specification).put("json", json);

		throw new CcpJsonInvalid(result);
	}

	private static CcpJsonRepresentation simpleValidation(
			ValidationRules rules, 
			CcpJsonRepresentation json,
			CcpJsonRepresentation result
			) {
		SimpleObject[] simples = rules.simpleObject();

		for (SimpleObject validation : simples) {
			
			String[] fields = validation.fields();
			
			SimpleObjectValidations rule = validation.rule();
			
			String completeRuleName = getCompleteRuleName(SimpleObject.class, rule);
			
			CcpJsonRepresentation errors = CcpConstants.EMPTY_JSON;
			
			for (String field : fields) {
				
				boolean validJson = rule.isValidJson(json,  field);
				
				if(validJson) {
					continue;
				}
				
				Object value = json.content.get(field);
				CcpJsonRepresentation fieldDetails = CcpConstants.EMPTY_JSON
						.put("name", field)
						.put("value", value)
						;
				errors = errors.addToList("wrongFields", fieldDetails);
				result = result.addToItem("errors", completeRuleName, errors);
			}
		}

		return result;
	}

	private static CcpJsonRepresentation getSpecification(String featureName, ValidationRules rules) {

		CcpJsonRepresentation specification = CcpConstants.EMPTY_JSON;
		
		AllowedValues[] allowedValues = rules.allowedValues();
		
		specification = specification.put("featureName", featureName);
		for (AllowedValues dumb : allowedValues) {

			String[] restrictedValues = dumb.allowedValues();
			AllowedValuesValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(AllowedValues.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "restrictedValues", restrictedValues);
		}
		
		ArrayNumbers[] arrayNumbers = rules.arrayNumbers();
		
		for (ArrayNumbers dumb : arrayNumbers) {
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			double bound = dumb.bound();
			
			String completeRuleName = getCompleteRuleName(ArrayNumbers.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);
		}
		
		ArraySize[] arraySize = rules.arraySize();
		
		for (ArraySize dumb : arraySize) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(ArraySize.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);

		}
		ArrayTextSize[] arrayTextSize = rules.arrayTextSize();
		
		for (ArrayTextSize dumb : arrayTextSize) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(ArrayTextSize.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);
		}
		
		Day[] day = rules.day();
		
		for (Day dumb : day) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(Day.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);

		}
		ObjectNumbers[] objectNumbers = rules.objectNumbers();
		for (ObjectNumbers dumb : objectNumbers) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(ObjectNumbers.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);

		}
		ObjectTextSize[] objectTextSize = rules.objectTextSize();
		for (ObjectTextSize dumb : objectTextSize) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(ObjectTextSize.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);

		}
		Regex[] regex = rules.regex();
		
		for (Regex dumb : regex) {
			String value = dumb.value();
			String[] fields = dumb.fields();
			
			String completeRuleName = Regex.class.getSimpleName();
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "value", value);

		}
		
		SimpleObject[] simpleObject = rules.simpleObject();
		
		for (SimpleObject dumb : simpleObject) {
			SimpleObjectValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(SimpleObject.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);

		}
		
		Year[] year = rules.year();
		for (Year dumb : year) {
			double bound = dumb.bound();
			BoundValidations rule = dumb.rule();
			String[] fields = dumb.fields();
			
			String completeRuleName = getCompleteRuleName(Year.class, rule);
			specification = specification.addToItem(completeRuleName, "evaluatedFields", fields);
			specification = specification.addToItem(completeRuleName, "bound", bound);

		}
		return specification;
	}
	
	private static String getCompleteRuleName(Class<?> ruleClazz, BoundValidations rule) {
		String completeRuleName = getCompleteRuleName(ruleClazz, (Enum<?>)rule);
		return completeRuleName;
	}

	private static CcpJsonRepresentation validateRestricted(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {
		AllowedValues[] restricteds = rules.allowedValues();

		for (AllowedValues validation : restricteds) {
			String[] restrictedValues = validation.allowedValues();
			String[] fields = validation.fields();
			AllowedValuesValidations rule = validation.rule();
			
			String completeRuleName = getCompleteRuleName(AllowedValues.class, rule);
			
			CcpJsonRepresentation errors = CcpConstants.EMPTY_JSON;
			
			for (String field : fields) {
				
				boolean validJson = rule.isValidJson(json, restrictedValues, field);
				
				if(validJson) {
					continue;
				}
				errors = errors.put("restrictedValues", restrictedValues);
				
				boolean containsKey = json.containsKey(field);
				if(containsKey) {
					continue;
				}
				
				Object value = json.get(field);
				CcpJsonRepresentation fieldDetails = CcpConstants.EMPTY_JSON
						.put("name", field)
						.put("value", value)
						;
				errors = errors.addToList("wrongFields", fieldDetails);
				result = result.addToItem("errors", completeRuleName, errors);
			}
		}
		return result;
	}

	private static CcpJsonRepresentation addErrorDetail(
			CcpJsonRepresentation result, 
			CcpJsonRepresentation json, 
			Class<?> ruleClass, 
			Object bound, 
			BoundValidations rule, 
			String... fields) {
		
		String completeRuleName = getCompleteRuleName(ruleClass, (Enum<?>)rule);
		
		CcpJsonRepresentation errors = CcpConstants.EMPTY_JSON;
		
		for (String field : fields) {
			
			Double boundValue = Double.valueOf("" + bound);
			
			boolean validJson = rule.isValidJson(json, boundValue, field);
			
			if(validJson) {
				continue;
			}
			errors = errors.put("bound", bound);
			Object value = json.get(field);
			CcpJsonRepresentation fieldDetails = CcpConstants.EMPTY_JSON
					.put("name", field)
					.put("value", value)
					;
			errors = errors.addToList("wrongFields", fieldDetails);
			result = result.addToItem("errors", completeRuleName, errors);
		}
		
		return result;
	}

	private static String getCompleteRuleName(Class<?> ruleClazz, Enum<?> enumClass) {
		
		String ruleClassName = ruleClazz.getSimpleName();
		
		String enumName = enumClass.name();

		String completeRuleName = ruleClassName + "." + enumName;
		
		return completeRuleName;
	}	
	
	private static CcpJsonRepresentation validateBounds(
			ValidationRules rules, 
			CcpJsonRepresentation json,
			CcpJsonRepresentation result) {

		{
			ArrayNumbers[] x1 = rules.arrayNumbers();

			for (ArrayNumbers validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, ArrayNumbers.class, bound, rule, fields);
			}
			
		}

		{
			ArraySize[] x1 = rules.arraySize();

			for (ArraySize validation : x1) {

				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, ArraySize.class, bound, rule, fields);
			}

		}
		{
			ArrayTextSize[] x1 = rules.arrayTextSize();

			for (ArrayTextSize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, ArrayTextSize.class, bound, rule, fields);
			}

		}
		{
			ObjectNumbers[] x1 = rules.objectNumbers();

			for (ObjectNumbers validation : x1) {

				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, ObjectNumbers.class, bound, rule, fields);
			}

		}
		{
			ObjectTextSize[] x1 = rules.objectTextSize();

			for (ObjectTextSize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, ObjectTextSize.class, bound, rule, fields);
			}

		}
		{
			Day[] x1 = rules.day();

			for (Day validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				result = addErrorDetail(result, json, Day.class, bound, rule, fields);
			}

		}
		{
			Year[] x1 = rules.year();

			for (Year validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();

				result = addErrorDetail(result, json, Year.class, bound, rule, fields);
			}

		}
		
		{
			Regex[] x1 = rules.regex();
			CcpJsonRepresentation errors = CcpConstants.EMPTY_JSON;
			for (Regex validation : x1) {
				String[] fields = validation.fields();
				String regex = validation.value();
				for (String field : fields) {
					boolean validJson = json.itIsTrueThatTheFollowingFields(field).ifTheyAreAll()
							.textsThenEachOneMatchesWithTheFollowingRegex(regex);

					if (validJson) {
						continue;
					}
					errors = errors.put("regex", regex);
					Object value = json.get(field);
					CcpJsonRepresentation fieldDetails = CcpConstants.EMPTY_JSON
							.put("name", field)
							.put("value", value)
							;
					errors = errors.addToList("wrongFields", fieldDetails);
					result = result.addToItem("errors", "regex", errors);
					
				}
			}
		}
		
		return result;
	}


}
