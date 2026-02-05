import tkinter as tk
import time

PIXELS_PER_METER = 1

class Square:
    def __init__(self, canvas, mass, x, y, vx, radius, color):
        self.canvas = canvas
        self.mass = mass
        self.x = x
        self.y = y
        self.vx = vx
        self.radius = radius
        self.color = color
        self.shape = canvas.create_rectangle(x - radius, y - radius, x + radius, y + radius, fill=color)
        self.speed_text = canvas.create_text(self.x, self.y - radius - 10, text=f"v: {self.vx:.2f} м/с", fill="black")

    def move(self):
        self.canvas.move(self.shape, self.vx * PIXELS_PER_METER, 0)
        self.x += self.vx * PIXELS_PER_METER
        self.canvas.coords(self.speed_text, self.x, self.y - self.radius - 10)
        self.canvas.itemconfig(self.speed_text, text=f"v: {self.vx:.2f} м/с")

    def detect_wall_collision(self, width):
        if self.x + self.radius >= width or self.x - self.radius <= 0:
            self.vx = -self.vx

    def detect_square_collision(self, other):
        dx = abs(other.x - self.x)
        if dx <= self.radius + other.radius:
            nx = (other.x - self.x) / dx
            v1n = self.vx * nx
            v2n = other.vx * nx
            p1 = 2 * other.mass / (self.mass + other.mass) * (v2n - v1n)
            self.vx += p1 * nx
            p2 = 2 * self.mass / (self.mass + other.mass) * (v1n - v2n)
            other.vx += p2 * nx

def draw_grid():
    for i in range(0, WIDTH, 50 * PIXELS_PER_METER):
        canvas.create_line(i, 0, i, HEIGHT, fill="lightgray")
    for i in range(0, HEIGHT, 50 * PIXELS_PER_METER):
        canvas.create_line(0, i, WIDTH, i, fill="lightgray")
    canvas.create_line(0, HEIGHT // 2, WIDTH, HEIGHT // 2, fill="black", width=2)
    canvas.create_line(50, 0, 50, HEIGHT, fill="black", width=2)

def animate():
    global running
    if running:
        return
    running = True
    while running:
        for square in squares:
            square.move()
            square.detect_wall_collision(WIDTH)
        for i in range(len(squares)):
            for j in range(i + 1, len(squares)):
                squares[i].detect_square_collision(squares[j])
        root.update()
        time.sleep(0.02)

def stop_simulation():
    global running
    running = False

def clear_simulation():
    global squares
    canvas.delete("all")
    squares = []
    draw_grid()

def start_simulation():
    stop_simulation()
    clear_simulation()
    mass1 = float(mass1_entry.get())
    mass2 = float(mass2_entry.get())
    vx1 = float(vx1_entry.get())
    vx2 = float(vx2_entry.get())
    radius1 = float(radius1_entry.get())
    radius2 = float(radius2_entry.get())
    global squares
    squares = [
        Square(canvas, mass=mass1, x=100 * PIXELS_PER_METER, y=HEIGHT // 2, vx=vx1, radius=radius1, color="red"),
        Square(canvas, mass=mass2, x=400 * PIXELS_PER_METER, y=HEIGHT // 2, vx=vx2, radius=radius2, color="blue")
    ]
    animate()

root = tk.Tk()
root.title("Абсолютно упругое нецентральное соударение двух тел с разными массами")

WIDTH, HEIGHT = 800, 300
canvas = tk.Canvas(root, width=WIDTH, height=HEIGHT, bg="white")
canvas.grid(row=0, column=0, columnspan=4)

tk.Label(root, text="Масса 1 (кг):").grid(row=1, column=0)
mass1_entry = tk.Entry(root)
mass1_entry.grid(row=1, column=1)
tk.Label(root, text="Скорость 1 (м/с):").grid(row=2, column=0)
vx1_entry = tk.Entry(root)
vx1_entry.grid(row=2, column=1)
tk.Label(root, text="Размер 1 (радиус, м):").grid(row=3, column=0)
radius1_entry = tk.Entry(root)
radius1_entry.grid(row=3, column=1)

tk.Label(root, text="Масса 2 (кг):").grid(row=1, column=2)
mass2_entry = tk.Entry(root)
mass2_entry.grid(row=1, column=3)
tk.Label(root, text="Скорость 2 (м/с):").grid(row=2, column=2)
vx2_entry = tk.Entry(root)
vx2_entry.grid(row=2, column=3)
tk.Label(root, text="Размер 2 (радиус, м):").grid(row=3, column=2)
radius2_entry = tk.Entry(root)
radius2_entry.grid(row=3, column=3)

start_button = tk.Button(root, text="Запуск симуляции", command=start_simulation)
start_button.grid(row=4, column=0, columnspan=2)

stop_button = tk.Button(root, text="Остановить симуляцию", command=stop_simulation)
stop_button.grid(row=4, column=2, columnspan=2)

squares = []
running = False
draw_grid()
root.mainloop()
