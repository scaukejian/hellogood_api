package com.hellogood.utils.easemob.vo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatGroupBody implements BodyWrapper {
    private String groupName;
    private String desc;
    private Boolean isPublic;
    private int maxUsers;
    private Boolean approval;
    private String owner;
    private String[] members;

    public ChatGroupBody(){
    }
    public ChatGroupBody(String groupName, String desc, Boolean isPublic, int maxUsers, Boolean approval, String owner, String[] members) {
        this.groupName = groupName;
        this.desc = desc;
        this.isPublic = isPublic;
        this.maxUsers = maxUsers;
        this.approval = approval;
        this.owner = owner;
        this.members = members;
    }

    
    public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}
	public void setApproval(Boolean approval) {
		this.approval = approval;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setMembers(String[] members) {
		this.members = members;
	}
	public String getGroupName() {
        return groupName;
    }

    public String getDesc() {
        return desc;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public int getMaxUsers() {
        return maxUsers;
    }

    public Boolean getApproval() {
        return approval;
    }

    public String getOwner() {
        return owner;
    }

    public String[] getMembers() {
        return members;
    }

    public ContainerNode<?> getBody() {
        ObjectNode body = JsonNodeFactory.instance.objectNode();
        body.put("groupname", groupName).put("desc", desc).put("public", isPublic).put("approval", approval).put("owner", owner);
        if(0 != maxUsers) {
            body.put("maxusers", maxUsers);
        }
        if(ArrayUtils.isNotEmpty(members)) {
            ArrayNode membersNode = body.putArray("members");
            for (String member : members) {
                membersNode.add(member);
            }
        }

        return body;
    }

    public Boolean validate() {
        return StringUtils.isNotBlank(groupName) && StringUtils.isNotBlank(desc) && null != isPublic && null != approval && StringUtils.isNotBlank(owner);
    }
}
