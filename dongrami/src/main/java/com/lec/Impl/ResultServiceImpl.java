package com.lec.Impl;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lec.dto.WebReadingDTO;
import com.lec.entity.Card;
import com.lec.entity.Subcategory;
import com.lec.entity.WebReading;
import com.lec.repository.CardRepository;
import com.lec.repository.ResultRepository;
import com.lec.repository.SubcategoryRepository;
  
@Service
public class ResultServiceImpl {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CardRepository cardRepository;
    
	public List<WebReadingDTO> getOneCardReadings(int subcategoryId) {
		  List<Object[]> results = resultRepository.findOneCardReadings(subcategoryId);
		  System.out.println("서비스에서 : " + results);
		  return results.stream()
		       .map(this::mapToOneReadingDTO)
		       .collect(Collectors.toList());
	}
	  
	private WebReadingDTO mapToOneReadingDTO(Object[] result) {
	    WebReadingDTO dto = new WebReadingDTO();
	    dto.setWebReadingId(result[0] != null ? (Integer) result[0] : null);
	    dto.setReading1(result[1] != null ? (String) result[1] : null);
	    dto.setBubbleSlackName(result[2] != null ? result[2].toString() : null);
	    Integer subcategoryId = result[3] != null ? (Integer) result[3] : null;
	    Integer cardId = result[4] != null ? (Integer) result[4] : null;
	    dto.setSubcategory(subcategoryId != null ? subcategoryRepository.findById(subcategoryId).orElse(null) : null);
	    dto.setCard(cardId != null ? cardRepository.findById(cardId).orElse(null) : null);
	    dto.setImageUrl1(result[5] != null ? (String) result[5] : null);
	    dto.setReading1Title(result[6] != null ? result[6].toString() : null);
	    
	    System.out.println("mapped dto : " + dto);
	    return dto;
	}
}





