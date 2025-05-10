class Person {
    private String nickname;
    private String phone;
    private String email;

    public Person(String nickname, String phone, String email) {
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
    }

    public String getNickname() { return nickname; }
    public String getPhone()    { return phone;    }
    public String getEmail()    { return email;    }
}
