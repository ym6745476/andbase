package com.andbase.demo.model;

import java.util.List;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
@Table(name = "local_user")
public class LocalUser {

	// ID @Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	private int _id;
	
	@Column(name = "u_id")
	private String uId;

	// 登录用户名 length=20数据字段的长度是20
	@Column(name = "name", length = 20)
	private String name;

	// 用户密码
	@Column(name = "password")
	private String password;

	// 年龄一般是数值,用type = "INTEGER"规范一下.
	@Column(name = "age", type = "INTEGER")
	private int age;

	// 创建时间
	@Column(name = "create_time")
	private String createTime;
	
	// 包含实体的存储，指定外键
	@Relations(name="stock",type="one2one",foreignKey = "u_id",action="query_insert")
	private Stock stock;

	// 包含List的存储，指定外键
	@Relations(name="stocks",type="one2many",foreignKey = "u_id",action="query_insert")
	private List<Stock> stocks;
	
	// 有些字段您可能不希望保存到数据库中,不用@Column注释就不会映射到数据库.
	private String remark;


	public int get_id() {
		return _id;
	}


	public void set_id(int _id) {
		this._id = _id;
	}


	public String getuId() {
		return uId;
	}


	public void setuId(String uId) {
		this.uId = uId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public List<Stock> getStocks() {
		return stocks;
	}


	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Stock getStock() {
		return stock;
	}


	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
