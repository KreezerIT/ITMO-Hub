import math
import tkinter as tk
from tkinter import messagebox


# Декартова -> Полярная
def cartesian_to_polar(x, y, precision):
    r = round(math.sqrt(x ** 2 + y ** 2), precision)
    theta = round(math.degrees(math.atan2(y, x)), precision)
    return r, theta

# Полярная -> Декартова
def polar_to_cartesian(r, theta, precision):
    theta = math.radians(theta)
    x = round(r * math.cos(theta), precision)
    y = round(r * math.sin(theta), precision)
    return x, y

# Декартова -> Цилиндрическая
def cartesian_to_cylindrical(x, y, z, precision):
    rho = round(math.sqrt(x ** 2 + y ** 2), precision)
    phi = round(math.degrees(math.atan2(y, x)), precision)
    return rho, phi, z

# Цилиндрическая -> Декартова
def cylindrical_to_cartesian(rho, phi, z, precision):
    phi = math.radians(phi)
    x = round(rho * math.cos(phi), precision)
    y = round(rho * math.sin(phi), precision)
    return x, y, z

# Декартова -> Сферическая
def cartesian_to_spherical(x, y, z, precision):
    r = round(math.sqrt(x ** 2 + y ** 2 + z ** 2), precision)
    theta = round(math.degrees(math.atan2(math.sqrt(x ** 2 + y ** 2), z)), precision)
    phi = round(math.degrees(math.atan2(y, x)), precision)
    return r, theta, phi

# Сферическая -> Декартова
def spherical_to_cartesian(r, theta, phi, precision):
    theta = math.radians(theta)
    phi = math.radians(phi)
    x = round(r * math.sin(theta) * math.cos(phi), precision)
    y = round(r * math.sin(theta) * math.sin(phi), precision)
    z = round(r * math.cos(theta), precision)
    return x, y, z

def convert():
    try:
        precision = int(entry_precision.get())

        result_text = "Результат:"

        if var_from.get() == 1:  # Декартова система
            x = float(entry_x.get())
            y = float(entry_y.get())
            z = float(entry_z.get())

            if var_to.get() == 2:  # Декартова -> Полярная
                r, theta = cartesian_to_polar(x, y, precision)
                result_text += f"\nr = {r}, θ = {theta}"
            elif var_to.get() == 3:  # Декартова -> Цилиндрическая
                rho, phi, z = cartesian_to_cylindrical(x, y, z, precision)
                result_text += f"\nρ = {rho}, φ = {phi}, z = {z}"
            elif var_to.get() == 4:  # Декартова -> Сферическая
                r, theta, phi = cartesian_to_spherical(x, y, z, precision)
                result_text += f"\nr = {r}, θ = {theta}, φ = {phi}"

        elif var_from.get() == 2:  # Полярная система
            r = float(entry_r.get())
            theta = float(entry_theta.get())

            if var_to.get() == 1:  # Полярная -> Декартова
                x, y = polar_to_cartesian(r, theta, precision)
                result_text += f"\nx = {x}, y = {y}"
            elif var_to.get() == 3:  # Полярная -> Цилиндрическая
                x, y = polar_to_cartesian(r, theta, precision)
                rho, phi, z = cartesian_to_cylindrical(x, y, 0, precision)  # z = 0 по умолчанию
                result_text += f"\nρ = {rho}, φ = {phi}, z = {z}"
            elif var_to.get() == 4:  # Полярная -> Сферическая
                x, y = polar_to_cartesian(r, theta, precision)
                r, theta, phi = cartesian_to_spherical(x, y, 0, precision)  # z = 0 по умолчанию
                result_text += f"\nr = {r}, θ = {theta}, φ = {phi}"

        elif var_from.get() == 3:  # Цилиндрическая система
            rho = float(entry_rho.get())
            phi = float(entry_phi.get())
            z = float(entry_z.get())

            if var_to.get() == 1:  # Цилиндрическая -> Декартова
                x, y, z = cylindrical_to_cartesian(rho, phi, z, precision)
                result_text += f"\nx = {x}, y = {y}, z = {z}"
            elif var_to.get() == 2:  # Цилиндрическая -> Полярная
                x, y, z = cylindrical_to_cartesian(rho, phi, z, precision)
                r, theta = cartesian_to_polar(x, y, precision)
                result_text += f"\nr = {r}, θ = {theta}"
            elif var_to.get() == 4:  # Цилиндрическая -> Сферическая
                x, y, z = cylindrical_to_cartesian(rho, phi, z, precision)
                r, theta, phi = cartesian_to_spherical(x, y, z, precision)
                result_text += f"\nr = {r}, θ = {theta}, φ = {phi}"

        elif var_from.get() == 4:  # Сферическая система
            r = float(entry_r.get())
            theta = float(entry_theta.get())
            phi = float(entry_phi.get())

            if var_to.get() == 1:  # Сферическая -> Декартова
                x, y, z = spherical_to_cartesian(r, theta, phi, precision)
                result_text += f"\nx = {x}, y = {y}, z = {z}"
            elif var_to.get() == 2:  # Сферическая -> Полярная
                x, y, z = spherical_to_cartesian(r, theta, phi, precision)
                r, theta = cartesian_to_polar(x, y, precision)
                result_text += f"\nr = {r}, θ = {theta}"
            elif var_to.get() == 3:  # Сферическая -> Цилиндрическая
                x, y, z = spherical_to_cartesian(r, theta, phi, precision)
                rho, phi, z = cartesian_to_cylindrical(x, y, z, precision)
                result_text += f"\nρ = {rho}, φ = {phi}, z = {z}"

        label_result.config(text=result_text)

    except ValueError:
        messagebox.showerror("Ошибка", "Неверные данные!")


root = tk.Tk()
root.title("Преобразование координат")
root.geometry("420x600")

canvas = tk.Canvas(root, width=400, height=600)
canvas.grid(row=0, column=0, columnspan=3, rowspan=20)

canvas.create_rectangle(0, 0, 400, 200, fill="white", outline="")
canvas.create_rectangle(0, 200, 400, 400, fill="blue", outline="")
canvas.create_rectangle(0, 400, 400, 600, fill="red", outline="")

var_from = tk.IntVar()
var_to = tk.IntVar()

label_from = tk.Label(root, text="Исходная система координат:", bg="white")
label_from.grid(row=0, column=0, padx=10, pady=10)

# Выбор исходной системы координат
var_from = tk.IntVar()
label_from = tk.Label(root, text="Исходная система координат:")
label_from.grid(row=0, column=0, padx=10, pady=10)

radio_cartesian_from = tk.Radiobutton(root, text="Декартова(x,y,z)", variable=var_from, value=1)
radio_cartesian_from.grid(row=1, column=0, sticky="W",padx=30)

radio_polar_from = tk.Radiobutton(root, text="Полярная(r,θ)", variable=var_from, value=2)
radio_polar_from.grid(row=2, column=0, sticky="W",padx=30)

radio_cylindrical_from = tk.Radiobutton
radio_cylindrical_from = tk.Radiobutton(root, text="Цилиндрическая(z,ρ,φ_Cyl)", variable=var_from, value=3)
radio_cylindrical_from.grid(row=3, column=0, sticky="W",padx=30)

radio_spherical_from = tk.Radiobutton(root, text="Сферическая(r,θ,φ_Sph)", variable=var_from, value=4)
radio_spherical_from.grid(row=4, column=0, sticky="W",padx=30)

# Выбор целевой системы координат
var_to = tk.IntVar()
label_to = tk.Label(root, text="Целевая система координат:")
label_to.grid(row=0, column=1, padx=10, pady=10)

radio_cartesian_to = tk.Radiobutton(root, text="Декартова", variable=var_to, value=1)
radio_cartesian_to.grid(row=1, column=1, sticky="W")

radio_polar_to = tk.Radiobutton(root, text="Полярная", variable=var_to, value=2)
radio_polar_to.grid(row=2, column=1, sticky="W")

radio_cylindrical_to = tk.Radiobutton(root, text="Цилиндрическая", variable=var_to, value=3)
radio_cylindrical_to.grid(row=3, column=1, sticky="W")

radio_spherical_to = tk.Radiobutton(root, text="Сферическая", variable=var_to, value=4)
radio_spherical_to.grid(row=4, column=1, sticky="W")

# Поля ввода для декартовой системы координат
label_x = tk.Label(root, text="x:")
label_x.grid(row=5, column=0, padx=10, pady=5)
entry_x = tk.Entry(root)
entry_x.grid(row=5, column=1, padx=10, pady=5)

label_y = tk.Label(root, text="y:")
label_y.grid(row=6, column=0, padx=10, pady=5)
entry_y = tk.Entry(root)
entry_y.grid(row=6, column=1, padx=10, pady=5)

label_z = tk.Label(root, text="z:")
label_z.grid(row=7, column=0, padx=10, pady=5)
entry_z = tk.Entry(root)
entry_z.grid(row=7, column=1, padx=10, pady=5)

# Поля ввода для полярных координат (r, θ)
label_r = tk.Label(root, text="r:")
label_r.grid(row=8, column=0, padx=10, pady=5)
entry_r = tk.Entry(root)
entry_r.grid(row=8, column=1, padx=10, pady=5)

label_theta = tk.Label(root, text="θ (в радианах):")
label_theta.grid(row=9, column=0, padx=10, pady=5)
entry_theta = tk.Entry(root)
entry_theta.grid(row=9, column=1, padx=10, pady=5)

# Поля ввода для цилиндрических координат (ρ, φ, z)
label_rho = tk.Label(root, text="ρ:")
label_rho.grid(row=10, column=0, padx=10, pady=5)
entry_rho = tk.Entry(root)
entry_rho.grid(row=10, column=1, padx=10, pady=5)

label_phi = tk.Label(root, text="φ_Sph (в радианах):")
label_phi.grid(row=11, column=0, padx=10, pady=5)
entry_phi = tk.Entry(root)
entry_phi.grid(row=11, column=1, padx=10, pady=5)

# Поля ввода для сферических координат (r, θ, φ)
label_phi_spherical = tk.Label(root, text="φ_Cyl (в радианах):")
label_phi_spherical.grid(row=12, column=0, padx=10, pady=5)
entry_phi_spherical = tk.Entry(root)
entry_phi_spherical.grid(row=12, column=1, padx=10, pady=5)


default_precision = tk.StringVar()
default_precision.set("2")
# Поле ввода для точности
label_precision = tk.Label(root, text="Точность (кол-во знаков после ):")
label_precision.grid(row=13, column=0, padx=10, pady=5)
entry_precision = tk.Entry(root,textvariable=default_precision)
entry_precision.grid(row=13, column=1, padx=10, pady=5)

button_convert = tk.Button(root, text="Преобразовать", command=convert)
button_convert.grid(row=14, column=0, columnspan=2, pady=10)

label_result = tk.Label(root, text="Результат:")
label_result.grid(row=15, column=0, columnspan=2, padx=10, pady=10)


root.mainloop()
