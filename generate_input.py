"""
python generate_input.py -r 10 -s 5 -o 200 -w 50 -m 100 > input_data.txt

-o 200 = amount of passangers is 200% higher than seats amount
-o 100 = amount of passangers is equal to seats amount
-o 50 = amount of passangers is only 50% of  to seats amount
default is 110%

-w 50 = 50% of passengers asked for window
-w 10 = 10% of passengers asked for window
-w 100 = All passengers asked for window
default is 25% (every fourth)

-m 100 = max group length is equal to size of 1 row (100%)
-m 50 = max group length is half of size of 1 row (50%)
-m 500 = max group length will be 5 times of 1 row size (500%)
default is 125%


"""
import argparse
import random

parser = argparse.ArgumentParser(
    description='Generates input data for airplain seats problem.')
parser.add_argument('-r', '--rows', metavar='rows', type=int,
                    help='number of rows', default=10)
parser.add_argument('-s', '--row-size', metavar='row_size', type=int,
                    help='size of the row', default=4)
parser.add_argument('-o', '--occupancy', metavar='occupancy', type=int,
                    help='percentage of filled seats', default=110)
parser.add_argument('-w', '--windows', metavar='windows', type=int,
                    help='percentage of windows', default=25)
parser.add_argument('-m', '--max-group-size', metavar='max_group_size', type=int,
                    help='groups size coefficient', default=125)

args = parser.parse_args()


MAX_GROUP_LEGTH_MULTIPLIER = 1


def gen_group(passengers_amount, windows):
    passengers = range(1, passengers_amount + 1)
    while passengers:
        group_size = random.randint(1, int(args.row_size * args.max_group_size / 100))
        group = passengers[:group_size+1]
        passengers = passengers[group_size+1:]

        windowed_group = []
        for i, seat in enumerate(group):
            windowed_group.append(str(seat))
            if windows[seat - 1]:
                windowed_group[i] += 'W'

        yield windowed_group


if __name__ == '__main__':
    print('%s %s' % (args.rows, args.row_size))
    passengers_amount = int(args.rows * args.row_size * args.occupancy / 100)

    windows_amount = int(passengers_amount * args.windows / 100)
    windows = [i < windows_amount for i in range(passengers_amount)]
    random.shuffle(windows)

    for group in gen_group(passengers_amount, windows):
        print(' '.join(group))
