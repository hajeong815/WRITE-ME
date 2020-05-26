package com.WRITEME.controller;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.WRITEME.dao.PostDAO;
import com.WRITEME.model.PostDTO;

@RestController
@EnableAutoConfiguration
@MapperScan(basePackages = "com.WRITEME.dao")
public class PostController {

	@Autowired
	private PostDAO postDAO;
	
	@RequestMapping(value = "/api/post", method = RequestMethod.POST)
	public PostDTO post(PostDTO post) throws Exception{
		postDAO.newPost(post);
		return post;
	}
	
	//postID로 게시물 불러오기
    @RequestMapping(value = "/api/post/{postID}", method = RequestMethod.GET)
    public ResponseEntity<PostDTO> getPost(@PathVariable("postID") final int postID) throws Exception {
        /* TODO: 조회수 증가 */
        PostDTO param = new PostDTO();
        param.setPostID(postID);
        
        PostDTO post = postDAO.getPost(param);
        
        if (post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(post, HttpStatus.OK);
    }
	
	
	//키워드 아이디로 게시물 불러오기
    @RequestMapping(value = "/api/post")
    public List<PostDTO> getPostbyKeywordID(@RequestParam(value = "keywordID", defaultValue = "") int keywordID) 
    		throws Exception {
        /* TODO: 조회수 증가 */
        final PostDTO param = new PostDTO(0, null, null, null, keywordID, 0, null);
        //`postID`, `postTitle`, `postDetail`, `userID`, `keywordID`, `categoryID`, `date`
        final List<PostDTO> postList = postDAO.getPostbyKeywordID(param);
        return postList;
    }
    
    
    //게시물 수정
    @RequestMapping(value = "/api/post/edit/{postID}", method = RequestMethod.PUT)
    public ResponseEntity<PostDTO> putPost(@PathVariable("postID") final int postID, PostDTO param) throws Exception {
    	if((param.getUserID() == null) || (param.getPostDetail() == null) || (param.getPostTitle() == null)) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    	
    	param.setPostID(postID); // 게시물 번호
    	PostDTO post = postDAO.getPost(param);
    	if (post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	
    	post.setPostTitle(param.getPostTitle());
    	post.setPostDetail(param.getPostDetail());
    	postDAO.editPost(post);
    	
    	return new ResponseEntity<>(post, HttpStatus.OK);
    	
    }
    
    
    
    
    /*
     *     @RequestMapping(value = "/board/{seq}", method = RequestMethod.PUT)
    public ResponseEntity<BoardDTO> putUsers(@PathVariable("seq") final int seq, BoardDTO param) throws Exception {
        if ((param.getAuthor() == null) || (param.getContents() == null) || (param.getPassword() == null) || (param.getTitle() == null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
 
        param.setSeq(seq); // 조회할 게시물 번호 지정
        BoardDTO board = boardDAO.getBoard(param);
        if (board == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
 
        board.setTitle(param.getTitle());
        board.setContents(param.getContents());
        board.setAuthor(param.getAuthor());
        boardDAO.editBoard(board);
 
        return new ResponseEntity<>(board, HttpStatus.OK);
    }
     */
    
    
  /*  @RequestMapping("api/posts/keyword")
    public List<KeywordsDTO> keywords(@RequestParam(value = "categoryID", defaultValue = "") int categoryID) 
    		throws Exception {
        final KeywordsDTO param = new KeywordsDTO(0, categoryID, null, null, null);
        final List<KeywordsDTO> keywordsList = keywordsDAO.selectKeywords(param);
        return keywordsList;
    }*/
    
}
