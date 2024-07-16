package com.lec.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lec.entity.Vote;
import com.lec.service.VoteService;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

	@Autowired
	private VoteService voteService;
	// 모든 투표 리스트 조회
	@GetMapping
	public List<Vote> getAllVotes() {
		return voteService.getAllVotes();
	}

	// 페이징
	  @GetMapping("/paged-votes")
	    public Page<Vote> getPagedVotes(@RequestParam("page") int page, @RequestParam("size") int size) {
	        return voteService.getPagedVotes(page, size);
	    }

	// 특정 ID의 투표 조회

	  @GetMapping("/{voteId}") 
	  public ResponseEntity<Vote> getVoteById(@PathVariable("voteId") int voteId) { 
		  Optional<Vote> vote = voteService.getVoteById(voteId); return
	  vote.map(ResponseEntity::ok).orElseGet(() ->
	  ResponseEntity.notFound().build()); 
	  }

	// 투표 생성
	@PostMapping
	public ResponseEntity<Vote> createVote(@RequestBody Vote vote) {
		Vote createdVote = voteService.createVote(vote);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdVote);
	}

	// 투표 업데이트

    @PutMapping("/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable("id") int id, @RequestBody Map<String, String> payload) {
        Optional<Vote> optionalVote = voteService.getVoteById(id);
        if (optionalVote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vote vote = optionalVote.get();
        String option = payload.get("option");
        String previousOption = payload.get("previousOption"); // 클라이언트에서 이전 옵션을 받아옴

        // 이전 선택을 취소
        if ("option1".equals(previousOption)) {
            vote.setOption1Count(vote.getOption1Count() - 1);
        } else if ("option2".equals(previousOption)) {
            vote.setOption2Count(vote.getOption2Count() - 1);
        }

        // 새로운 선택 반영
        if ("option1".equals(option)) {
            vote.setOption1Count(vote.getOption1Count() + 1);
        } else if ("option2".equals(option)) {
            vote.setOption2Count(vote.getOption2Count() + 1);
        } else {
            return ResponseEntity.badRequest().build();
        }

        Vote updatedVote = voteService.updateVote(vote);
        return ResponseEntity.ok(updatedVote);
    }
	

	// 투표 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteVote(@PathVariable int id) {
		if (voteService.getVoteById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		voteService.deleteVote(id);
		return ResponseEntity.noContent().build();
	}

    
    
    @GetMapping("/votes")
    public String showVotesPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,Model model) {
        Page<Vote> votePage = voteService.getPagedVotes(page, size);
        
        model.addAttribute("votes", votePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", votePage.getTotalPages());
        
        return "votes"; // 이 부분은 Thymeleaf 템플릿 이름에 해당
    }
}


