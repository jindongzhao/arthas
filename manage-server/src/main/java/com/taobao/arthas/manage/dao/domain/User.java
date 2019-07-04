package com.taobao.arthas.manage.dao.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created on 2018/3/23.
 *
 * @author zlf
 * @since 1.0
 */
@Entity
@Table(name = "test_user")
public class User extends BaseDo {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;// 主键

	@Column(nullable = false)
	private String firstName;// 名

	@Column(nullable = false)
	private String lastName;// 姓

	@Column(nullable = false)
	private String idCard;// 身份证

	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;// 出生日期

	public User() {
	}

	public User(Long id, String firstName, String lastName, String idCard, Date dateOfBirth) {
		//this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idCard = idCard;
		this.dateOfBirth = dateOfBirth;
	}

	/*public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}*/

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}


	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "User{" +
				//"id=" + id +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", idCard='" + idCard + '\'' +
				", dateOfBirth=" + dateOfBirth +
				'}';
	}

}
