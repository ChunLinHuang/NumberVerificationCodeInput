# 一个简单的数字验证码输入框，能自定义输入框个数和样式
## 快捷使用：
```
<hcl.numberverificationcodeinput.VerificationCodeInput
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:box="4"
        app:box_bg="@drawable/verification_box_bg"
        app:child_h_padding="10dp"
        app:text_color="@android:color/holo_red_dark"
        app:box_bg_color="@android:color/holo_blue_bright"
        android:id="@+id/code_input"
        ></hcl.numberverificationcodeinput.VerificationCodeInput> 
        ```
## 注意：textview背景，优先使用 box_bg属性，在使用box_bg_color属性
### 监听输入完成：
```
VerificationCodeInput code_input = (VerificationCodeInput) findViewById(R.id.code_input);
        code_input.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
           ```
## 属性介绍：
```
box:设置输入框数量，默认4个
child_h_padding：设置输入框水平方向间隔
child_width：设置每个box宽
child_height：设置每个box高
box_bg：设置box背景--drawable，当和box_bg_color一起使用时，优先使用box_bg
box_bg_color：设置box颜色---color，当和box_bg一起使用时，优先使用box_bg
text_color：设置box文本颜色
   ```
