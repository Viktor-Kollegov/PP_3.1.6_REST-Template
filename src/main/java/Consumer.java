import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/*
Список URL для операций и типы запросов:

Получение всех пользователей - …/api/users ( GET )
Добавление пользователя - …/api/users ( POST )
Изменение пользователя - …/api/users ( PUT )
Удаление пользователя - …/api/users /{id} ( DELETE )
 */
/*
Получить список всех пользователей
 Когда вы получите ответ на свой первый запрос, вы должны сохранить свой session id, который получен через cookie.
  Вы получите его в заголовке ответа set-cookie. Поскольку все действия происходят в рамках одной сессии,
  все дальнейшие запросы должны использовать полученный session id ( необходимо использовать заголовок в последующих запросах )
 Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор. В случае успеха вы получите
 первую часть кода.
 Изменить пользователя с id = 3. Необходимо поменять name на Thomas, а lastName на Shelby. В случае успеха вы получите
 еще одну часть кода.
 Удалить пользователя с id = 3. В случае успеха вы получите последнюю часть кода.
В результате выполненных операций вы должны получить итоговый код, сконкатенировав все его части. Количество символов
 в коде = 18.
 */
public class Consumer {
    public static String url = "http://94.198.50.185:7081/api/users";
    public static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        HttpHeaders httpHeaders = new HttpHeaders(); // Создаём пустые заголовки
        httpHeaders.setContentType(MediaType.APPLICATION_JSON); // Типа JSON

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders); // Создаём сущность http запроса и кладём в GET запрос

        ResponseEntity<List> responseEntity = getUsersList(httpEntity); // Получаем ответ
        HttpHeaders responseHeaders = responseEntity.getHeaders(); // Достаём переданные нам заголовки
        System.out.println("responseHeaders = " + responseHeaders);

        System.out.println("Cookie = " + responseEntity.getHeaders().get("Set-Cookie"));
        httpHeaders.set("Cookie", String.join(";", Objects.requireNonNull(responseEntity.getHeaders().get("Set-Cookie"))));
        // Для дальнейших запросов задаём заголовок с нашим JSESSIONID

        User newUser = new User();
        newUser.setId(3L);
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 30);
        httpEntity = new HttpEntity<>(newUser, httpHeaders); // Создаём новую сущность http запроса и кладём в POST запрос
        // с нашими куки и юзером
        String part1 = addUser(httpEntity);
        System.out.println("part1 = " + part1);

        newUser.setName("Thomas");
        newUser.setLastName("Shelby");
        httpEntity = new HttpEntity<>(newUser, httpHeaders);
        String part2 = updateUser(httpEntity);
        System.out.println("part2 = " + part2);

        String part3 = deleteUser(httpEntity, newUser);
        System.out.println("part3 = " + part3);

        System.out.println(part1 + part2 + part3);
    }


    public static ResponseEntity<List> getUsersList(HttpEntity<Object> httpEntity) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, List.class);
    }

    public static String addUser(HttpEntity<Object> httpEntity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public static String updateUser(HttpEntity<Object> httpEntity) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);
        return responseEntity.getBody();
    }

    public static String deleteUser(HttpEntity<Object> httpEntity, User user) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/" + user.getId(), HttpMethod.DELETE, httpEntity, String.class);
        return responseEntity.getBody();
    }
}