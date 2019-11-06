package com.lazhu.generate.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典
 * @author naxj
 *
 */
public class DicValue {

		private Long id;
		private String type;
		private String code;
		private String codeText;
		private Integer sortNO;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getCodeText() {
			return codeText;
		}
		public void setCodeText(String codeText) {
			this.codeText = codeText;
		}
		public Integer getSortNO() {
			return sortNO;
		}
		public void setSortNO(Integer sortNO) {
			this.sortNO = sortNO;
		}
}
