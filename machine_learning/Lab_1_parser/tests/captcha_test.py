from edc_parser.captcha import _is_captcha_page, _parse_math_captcha_and_payload

# Страница с капчей зарезолвлена
def test_captcha_example_page_detected_and_solved():
    html = """
    <!DOCTYPE html>
    <html lang="ru">
    <head>
    <meta charset="UTF-8">
    <title>Проверка на бота</title>
    </head>
    <body style="text-align:center;margin-top:100px;font-family:sans-serif;">
        <h2>Подтвердите, что вы не бот</h2>
        <form method="post" style="margin-top:20px;font-size:16px;">
            <label>Сколько будет: 2 + 1 = ?</label><br><br>
            <input type="text" name="captcha" required>
            <button type="submit">Отправить</button>
        </form>
    </body>
    </html>
    """
    assert _is_captcha_page(html) is True
    action, payload = _parse_math_captcha_and_payload(html)
    assert action is None
    assert payload.get("captcha") == "3"
