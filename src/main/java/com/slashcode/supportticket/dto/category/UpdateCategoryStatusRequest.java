package com.slashcode.supportticket.dto.category;

import jakarta.validation.constraints.NotNull;

public class UpdateCategoryStatusRequest {

	@NotNull(message= "Status is required")
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
