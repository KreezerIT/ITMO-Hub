import pytest
from edc_parser.listings import ObjectListing
from datetime import datetime, timedelta

CASES = [
    # 1. Квартира, описание с "100 метрах ... 3 комнаты"
    (
        "apartment_description_rooms",
        """
        <html><head><title>Продам квартиру в центре</title></head>
        <body>
          <div class="vw-descr c-article" id="j-descr" itemprop="description">
            Продаётся дом в экологически чистом посёлке, в 100 метрах от реки Кубань, 3 комнаты, прихожая.
          </div>
          <div class="vw-dynprops-item">
            <div class="vw-dynprops-item-attr">Площадь:</div>
            <div class="vw-dynprops-item-val">56 м²</div>
          </div>
        </body></html>
        """,
        "https://edc.sale/ru/kursk/real-estate/sale/apartments/1/obyavlenie-123.html",
        {"rooms": 3, "object_type": "apartments", "floors_total": None},
    ),
    # 2. Дом, rooms=None, floors_total=2
    (
        "house_no_rooms",
        """
        <html><head><title>Дом 2 эт.</title></head>
        <body>
          <div class=vw-descr>Отличный дом, 2 эт., большая территория.</div>
        </body></html>
        """,
        "https://edc.sale/ru/krasnodar/real-estate/sale/houses/dom-klassnyj-999.html",
        {"rooms": None, "object_type": "houses", "floors_total": 2},
    ),
    # 3. Земля, area_m2=588 (5.88 соток)
    (
        "land_area_from_dynprops",
        """
        <html>
          <head><title>Дача в черте города Владимир — выгодно</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Адрес:</div>
              <div class="vw-dynprops-item-val">Владимир, 100, </div>
            </div>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Использование земли:</div>
              <div class="vw-dynprops-item-val">Индивидуальное жилищное строительство, Садоводство</div>
            </div>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь:</div>
              <div class="vw-dynprops-item-val">5.88 соток</div>
            </div>
            <div class="vw-descr">Ухоженный садовый участок в 100 м от роддома...</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/vladimir/real-estate/sale/land/dacha-v-cherte-goroda-vladimir-2136053.html",
        {"object_type": "land", "area_m2": 588.0, "floors_total": None},
    ),
    # 4. Квартира-студия, rooms=1
    (
        "studio_no_explicit_rooms",
        """
        <html>
          <head><title>Квартира-студия в новом доме</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь:</div>
              <div class="vw-dynprops-item-val">28 м²</div>
            </div>
            <div class="vw-descr">Уютная студия рядом с метро.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/kvartira-studiya-111.html",
        {"rooms": 1, "object_type": "apartments"},
    ),
    # 5. Квартира-студия с явным числом комнат
    (
        "studio_with_explicit_rooms",
        """
        <html>
          <head><title>Квартира студия с ремонтом</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Количество комнат:</div>
              <div class="vw-dynprops-item-val">2</div>
            </div>
            <div class="vw-descr">Современная планировка, светлые тона.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/kvartira-studiya-112.html",
        {"rooms": 2, "object_type": "apartments"},
    ),
    # 6. Коммерческий склад, rooms=None
    (
        "commercial_storage_no_rooms",
        """
        <html>
          <head><title>Собственник сдает Склад, 540 кв.м. - 1260 кв.м – Люберцы – Объявления на «EDC.SALE»</title></head>
          <body>
            <div class="vw-descr">Складские площади различной площади.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/lubercy/real-estate/commercial/rent/storage/sobstvennik-sdajet-sklad-540-kvm-1260-kvm-2134219.html",
        {"rooms": None, "object_type": "storage"},
    ),
    # 7. Описание с "Высота потолков: 4 м; • Этаж: 1;"
    (
        "description_height_then_floor",
        """
        <html>
          <head><title>Коммерческое помещение</title></head>
          <body>
            <div class="vw-descr">Описание: Высота потолков: 4 м; • Этаж: 1; • Расположение отличное.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/sankt-peterburg/real-estate/commercial/sale/shop/test-xxx.html",
        {"floor": 1, "floors_total": None},
    ),
    # 8. Пунктированный список, "на 1 (первом) этаже"
    (
        "bullet_list_after_etazhe",
        """
        <html>
          <head><title>Помещение с отдельным входом</title></head>
          <body>
            <div class="vw-descr">3. помещение с отдельным входом на 1 (первом) этаже, пройти мимо невозможно
            4. договороспособный собственник, соблюдающий интересы арендатора</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/commercial/rent/shop/test-floor-bullet.html",
        {"floor": 1, "floors_total": None},
    ),
    # 9. Цена и валюта из блока vw-price-box
    (
        "price_and_currency_from_vw_price_box",
        """
        <html>
          <head><title>Квартира</title></head>
          <body>
            <div class="vw-price-box">
                <span class="vw-price-num">100 RUB</span>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/price-test.html",
        {"price": 100, "currency": "RUB"},
    ),
    # 10. Дата создания объявления
    (
        "creation_date_parsed",
        """
        <html>
          <head><title>Объявление</title></head>
          <body>
            <div class="l-page-item-r-info d-none d-md-block">
              "#2135961 "<br>
              <span class="nowrap">Создано: 23 сентября</span>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/creation-date-test.html",
        {"creation_date": "23.09.2025"},
    ),
    # 11. Этажность из dynprops: 9
    (
        "floors_total_from_dynprops",
        """
        <html>
          <head><title>Квартира</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Этажность:</div>
              <div class="vw-dynprops-item-val">9</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/floors-total-only.html",
        {"floor": None, "floors_total": 9},
    ),
    # 12. Офис, описание "7 этажное здание"
    (
        "office_7_etazhnoe_description_only",
        """
        <html>
          <head><title>Офис в бизнес-центре</title></head>
          <body>
            <div class="vw-descr">Красивое отдельно стоящее 7 этажное здание. Прeстижный деловoй цeнтр. Удобное меcтоpаcпoложeниe.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/commercial/rent/office/obyavlenie-xxx.html",
        {"floor": None, "floors_total": 7, "object_type": "office"},
    ),
    # 13. Квартира, "на 3 эт."
    (
        "apartment_na_3_et_floor_only",
        """
        <html>
          <head><title>Квартира на 3 эт.</title></head>
          <body>
            <div class="vw-descr">Уютная квартира.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/kvartira-na-3-et.html",
        {"floor": 3, "floors_total": None},
    ),
    # 14. Офис, "на 3 эт."
    (
        "office_na_3_et_floor_only",
        """
        <html>
          <head><title>Офис на 3 эт.</title></head>
          <body>
            <div class="vw-descr">Офисное помещение.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/commercial/rent/office/ofis-na-3-et.html",
        {"floor": 3, "floors_total": None},
    ),
    # 15. Город и тип сделки из URL
    (
        "city_and_deal_from_url",
        """
        <html>
          <head><title>Объявление</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/nizhniy-novgorod/real-estate/sale/apartments/test.html",
        {"city": "Nizhniy Novgorod", "deal_type": "sell", "object_type": "apartments"},
    ),
    # 16. Папка изображений из data-thumb
    (
        "images_folder_from_data_thumb",
        """
        <html>
          <head><title>Фото</title></head>
          <body>
            <div class="gal"><div class="ph" data-thumb="/uploads/photos/abc/1.jpg"></div></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/test-img.html",
        {"images_folder_url": "https://edc.sale/uploads/photos/abc/"},
    ),
    # 17. Цена за м² рассчитывается
    (
        "price_per_m2_calc",
        """
        <html>
          <head><title>Квартира</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь:</div>
              <div class="vw-dynprops-item-val">50 м²</div>
            </div>
            <div class="vw-price-box"><span class="vw-price-num">200000 RUB</span></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/price-per-m2.html",
        {"area_m2": 50.0, "price": 200000, "currency": "RUB", "price_per_m2": 4000.0},
    ),
    # 18. Площадь 'кв. м' в описании
    (
        "area_kw_m_in_descr",
        """
        <html>
          <head><title>Объект</title></head>
          <body>
            <div class="vw-descr">Общая площадь 35 кв. м, отличный ремонт.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/area-kwm.html",
        {"area_m2": 35.0},
    ),
    # 19. Этаж в формате X/Y в dynprops
    (
        "floor_x_y_in_dynprops",
        """
        <html>
          <head><title>Квартира</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Этаж:</div>
              <div class="vw-dynprops-item-val">3/9</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/floor-x-y.html",
        {"floor": 3, "floors_total": 9},
    ),
    # 20. Этажность из 'Этажей:'
    (
        "floors_total_from_etazhey",
        """
        <html>
          <head><title>Дом</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Этажей:</div>
              <div class="vw-dynprops-item-val">9</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/etazhey.html",
        {"floors_total": 9},
    ),
    # 21. Для домов этаж None даже при X/Y
    (
        "house_floor_none_even_with_xy",
        """
        <html>
          <head><title>Дом</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Этаж:</div>
              <div class="vw-dynprops-item-val">1/2</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/orel/real-estate/sale/houses/dom-s-uchastkom.html",
        {"object_type": "houses", "floor": None, "floors_total": 2},
    ),
    # 22. Для земли этажность принудительно None
    (
        "land_floors_total_forced_none",
        """
        <html>
          <head><title>Участок</title></head>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Этажность:</div>
              <div class="vw-dynprops-item-val">9</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/tula/real-estate/sale/land/uchastok.html",
        {"object_type": "land", "floors_total": None},
    ),
    # 23. Комнаты из формата "1-к" в title
    (
        "rooms_from_1k_title",
        """
        <html>
          <head><title>1-к квартира в центре</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/1k-test.html",
        {"rooms": 1, "object_type": "apartments"},
    ),
    # 24. Валюта верхним регистром независимо от ввода
    (
        "currency_uppercase",
        """
        <html>
          <head><title>Квартира</title></head>
          <body>
            <div class="vw-price-box">
                <span class="vw-price-num">250 usd</span>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/currency-test.html",
        {"price": 250, "currency": "USD"},
    ),
    # 25. Цена с разделителями и валюта в нижнем регистре
    (
        "price_with_spaces_and_lower_currency",
        """
        <html>
          <head><title>Цена</title></head>
          <body>
            <div class="vw-price-box"><span class="vw-price-num">1 234 567 uah</span></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/kyiv/real-estate/sale/apartments/price-lower.html",
        {"price": 1234567, "currency": "UAH"},
    ),
    # 26. Нет data-thumb -> images_folder_url=None
    (
        "no_images_folder",
        """
        <html>
          <head><title>Без фото</title></head>
          <body>
            <div class="vw-descr">Текст без галереи</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/no-images.html",
        {"images_folder_url": None},
    ),
    # 27. deal=rent и object=office при наличии commercial в URL
    (
        "deal_rent_object_office",
        """
        <html>
          <head><title>Офис</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/commercial/rent/office/sdam-ofis.html",
        {"deal_type": "rent", "object_type": "office"},
    ),
    # 28. Этаж и этажность из title "3/15 этаж"
    (
        "floor_total_from_title",
        """
        <html>
          <head><title>Квартира 3/15 этаж</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/title-floor.html",
        {"floor": 3, "floors_total": 15},
    ),
    # 29. Площадь из title с вариантом "м2"
    (
        "area_from_title_m2_variant",
        """
        <html>
          <head><title>Пентхаус 120 м2 с видом</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/sochi/real-estate/sale/apartments/area-title-m2.html",
        {"area_m2": 120.0},
    ),
    # 30. Дата с явным годом
    (
        "creation_date_with_year",
        """
        <html>
          <body>
            <div class="l-page-item-r-info d-none d-md-block">
              <span class="nowrap">Создано: 01 января 2024</span>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/sale/apartments/creation-date-year.html",
        {"creation_date": "01.01.2024"},
    ),
    # 31. Цена есть, площади нет -> price_per_m2=None
    (
        "price_per_m2_none_without_area",
        """
        <html>
          <body>
            <div class="vw-price-box"><span class="vw-price-num">100 USD</span></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/riga/real-estate/sale/apartments/no-area.html",
        {"price": 100, "currency": "USD", "price_per_m2": None},
    ),
    # 32. Площадь есть, цены нет -> price_per_m2=None
    (
        "price_per_m2_none_without_price",
        """
        <html>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь:</div>
              <div class="vw-dynprops-item-val">42 м²</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/minsk/real-estate/sale/apartments/no-price.html",
        {"area_m2": 42.0, "price": None, "currency": None, "price_per_m2": None},
    ),
    # 33. Объект "rooms" и комнаты из title
    (
        "rooms_object_type_parsing",
        """
        <html>
          <head><title>1-комнатная комната в центре</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/moskva/real-estate/rent/rooms/room-1.html",
        {"object_type": "rooms", "deal_type": "rent", "rooms": 1},
    ),
    # 34. url сохраняется без изменений
    (
        "url_preserved",
        """
        <html><head><title>Просто объект</title></head><body></body></html>
        """,
        "https://edc.sale/ru/kazan/real-estate/sale/apartments/url-check.html",
        {"url": "https://edc.sale/ru/kazan/real-estate/sale/apartments/url-check.html"},
    ),
    # 35. Studio на английском в title => rooms=1 (квартира)
    (
        "english_studio_in_title",
        """
        <html>
          <head><title>Cozy Studio apartment 25 m2</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/spb/real-estate/sale/apartments/studio-en.html",
        {"object_type": "apartments", "rooms": 1},
    ),
    # 36. building: floor подавляется, но floors_total берётся из title X/Y
    (
        "building_suppresses_floor",
        """
        <html>
          <head><title>Здание 3/9 этаж</title></head>
          <body></body>
        </html>
        """,
        "https://edc.sale/ru/ekb/real-estate/sale/building/zdanie.html",
        {"object_type": "building", "floor": None, "floors_total": 9},
    ),
    # 37. Абсолютный data-thumb => папка корректно вычисляется
    (
        "images_folder_from_absolute_thumb",
        """
        <html>
          <body>
            <div class="gal"><div class="ph" data-thumb="https://edc.sale/uploads/photos/xyz/10.jpg"></div></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/ufa/real-estate/sale/apartments/abs-thumb.html",
        {"images_folder_url": "https://edc.sale/uploads/photos/xyz/"},
    ),
    # 38. one-day в URL: объект и сделка распознаются
    (
        "deal_and_object_with_one_day",
        """
        <html><body></body></html>
        """,
        "https://edc.sale/ru/moskva/real-estate/rent/one-day/office/sutochno.html",
        {"deal_type": "rent", "object_type": "office"},
    ),
    # 39. Город с percent-encoding
    (
        "city_percent_encoded",
        """
        <html><body></body></html>
        """,
        "https://edc.sale/ru/nizhniy%20novgorod/real-estate/sale/apartments/enc.html",
        {"city": "Nizhniy Novgorod", "object_type": "apartments", "deal_type": "sell"},
    ),
    # 40. Площадь в описании: м^2 вариант
    (
        "area_m_caret_2_variant",
        """
        <html>
          <body>
            <div class="vw-descr">Площадь помещения 65 м^2, хороший ремонт.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/tver/real-estate/sale/apartments/m-caret2.html",
        {"area_m2": 65.0},
    ),
    # 41. Площадь без пробела: 70кв.м
    (
        "area_compact_kvm",
        """
        <html>
          <body>
            <div class="vw-descr">Площадь 70кв.м, центр города.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/izhevsk/real-estate/sale/apartments/70kvm.html",
        {"area_m2": 70.0},
    ),
    # 42. Валюта BYN в нижнем регистре
    (
        "currency_byn",
        """
        <html>
          <body>
            <div class="vw-price-box"><span class="vw-price-num">999 byn</span></div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/minsk/real-estate/sale/apartments/byn.html",
        {"price": 999, "currency": "BYN"},
    ),
    # 43. 'м' без квадрата не считается м²
    (
        "area_single_m_not_sqm",
        """
        <html>
          <body>
            <div class="vw-descr">Расстояние до метро 300 м. Хороший район.</div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/perm/real-estate/sale/apartments/not-sqm.html",
        {"area_m2": None},
    ),
    # 44. Для квартир 'Площадь участка' игнорируется
    (
        "apartment_ignores_plot_area",
        """
        <html>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь участка:</div>
              <div class="vw-dynprops-item-val">6 соток</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/ryazan/real-estate/sale/apartments/plot-ignored.html",
        {"object_type": "apartments", "area_m2": None},
    ),
    # 45. Для земли 'Площадь участка' в сотках конвертируется
    (
        "land_plot_area_converted",
        """
        <html>
          <body>
            <div class="vw-dynprops-item">
              <div class="vw-dynprops-item-attr">Площадь участка:</div>
              <div class="vw-dynprops-item-val">6 соток</div>
            </div>
          </body>
        </html>
        """,
        "https://edc.sale/ru/yaroslavl/real-estate/sale/land/plot-6-sotok.html",
        {"object_type": "land", "area_m2": 600.0},
    ),
]

@pytest.mark.parametrize("case_name, html, url, expected", CASES)
def test_ObjectListing_parsing(case_name, html, url, expected):
    listing = ObjectListing.from_html(html, url=url)
    for field, exp in expected.items():
        val = getattr(listing, field)
        assert val == exp, f"{case_name}: {field}={val!r}, expected {exp!r}"

# 46. Дата создания "сегодня"
def test_creation_date_today():
    html = f"""
    <html>
      <body>
        <div class="l-page-item-r-info d-none d-md-block">
          <span class="nowrap">Создано: сегодня</span>
        </div>
      </body>
    </html>
    """
    url = "https://edc.sale/ru/moskva/real-estate/sale/apartments/creation-date-today.html"
    listing = ObjectListing.from_html(html, url=url)
    expected = f"{datetime.now().day:02d}.{datetime.now().month:02d}.{datetime.now().year:02d}"
    assert listing.creation_date == expected

# 47. Дата создания "вчера"
def test_creation_date_yesterday():
    html = f"""
    <html>
      <body>
        <div class="l-page-item-r-info d-none d-md-block">
          <span class="nowrap">Создано: вчера</span>
        </div>
      </body>
    </html>
    """
    url = "https://edc.sale/ru/moskva/real-estate/sale/apartments/creation-date-yesterday.html"
    listing = ObjectListing.from_html(html, url=url)
    y = datetime.now() - timedelta(days=1)
    expected = f"{y.day:02d}.{y.month:02d}.{y.year}"
    assert listing.creation_date == expected
