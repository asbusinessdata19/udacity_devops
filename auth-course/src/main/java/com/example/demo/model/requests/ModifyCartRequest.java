package com.example.demo.model.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCartRequest {
	private String username;
	private long itemId;
	private int quantity;

}
