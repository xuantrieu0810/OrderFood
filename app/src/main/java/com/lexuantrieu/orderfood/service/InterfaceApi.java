package com.lexuantrieu.orderfood.service;

public interface InterfaceApi {
    /*@POST("usersInfo/Authenticate")
    Call<LoginResponse> getLoginResponse(
            @Header("Token") String token,
            @Body LoginDatum loginDatum
    );*/



  /*  InterfaceApi api = ApiClient.getClient().create(InterfaceApi.class);
    Call<LoginResponse> call = api.getLoginResponse(sendToken, loginDatum);

    call.enqueue(new Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            LoginResponse loginResponse = response.body();
            String token = response.headers().get("Token");
            if (token != null) {
                Log.e("tokenTAG", "Token : " + token);
                sendToken = token;
            }
            Toast.makeText(context, loginResponse.getStatusMessage() + "", Toast.LENGTH_SHORT).show();

            loadProgress.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            loadProgress.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    });
}*/
}
