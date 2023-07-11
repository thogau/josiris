package net.thogau.josiris.views.patient;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeItem {

	private String label;
	private String value;

	@Builder.Default
	private List<TreeItem> children = new ArrayList<>();
}
