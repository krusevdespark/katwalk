package net.shiftinpower.objects;

public class Comment { // Extended by ItemComment, PhotoComment and EventComment

	private int commentId;
	private String commentContent;
	private String commentDateCreated;
	private String commentDateEdited;
	private int commenterId;
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public String getCommentDateCreated() {
		return commentDateCreated;
	}
	public void setCommentDateCreated(String commentDateCreated) {
		this.commentDateCreated = commentDateCreated;
	}
	public String getCommentDateEdited() {
		return commentDateEdited;
	}
	public void setCommentDateEdited(String commentDateEdited) {
		this.commentDateEdited = commentDateEdited;
	}
	public int getCommenterId() {
		return commenterId;
	}
	public void setCommenterId(int commenterId) {
		this.commenterId = commenterId;
	}
	
	
}
