package cn.flying.rest.onlinefile.entity;

import java.io.Serializable;

public class UserInfo implements Serializable, IPropertyCopy<UserInfo> {
	private static final long serialVersionUID = -795881718758221317L;
	// 用户的id
	private String userId;
	// 显示的名称
	private String showName;

	public UserInfo() {
		super();
	}

	public UserInfo(String userId, String showName) {
		super();
		this.userId = userId;
		this.showName = showName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	private boolean isEmpty(String key) {
		return key == null || "".equals(key);
	}

	public UserInfo copyProperty(UserInfo entity) {
		if (!isEmpty(entity.getShowName())) {
			this.showName = entity.getShowName();
		}
		if (!isEmpty(entity.getUserId())) {
			this.userId = entity.getUserId();
		}
		return this;
	}

}
