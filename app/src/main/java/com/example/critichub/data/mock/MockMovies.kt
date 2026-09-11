package com.example.critichub.data.mock

import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.CriticReview
import com.example.critichub.model.Genre
import com.example.critichub.model.Person
import com.example.critichub.model.UserComment

/**
 * دادهٔ نمونهٔ فیلم‌ها (فقط برای نسخهٔ نمایشی؛ در آینده از API دریافت می‌شود).
 */
object MockMovies {

    val all: List<CatalogItem> = listOf(
        m(
            id = "mv-inception",
            titleFa = "تلقین",
            titleEn = "Inception",
            year = 2010,
            rating = 8.8,
            votes = 2_600_000,
            runtime = 148,
            genres = g(MockGenres.ACTION, MockGenres.SCIFI, MockGenres.THRILLER),
            trending = true,
            synopsis = "دوم کاب دزدی حرفه‌ای است که با ورود به رویاهای دیگران، اسرار ذهن آن‌ها را می‌دزدد؛ مهارتی که او را به فراری همیشگی تبدیل کرده است. حالا برای بازگشت به زندگی و فرزندانش باید غیرممکن‌ترین کار را انجام دهد: کاشتن یک ایده در عمیق‌ترین لایهٔ ذهن، کاری که به آن «تلقین» می‌گویند.",
            director = p("کریستوفر نولان", "Christopher Nolan"),
            cast = cast(
                p("لئوناردو دی‌کاپریو", "Leonardo DiCaprio"),
                p("جوزف گوردون-لویت", "Joseph Gordon-Levitt"),
                p("الن پیج", "Elliot Page"),
                p("تام هاردی", "Tom Hardy"),
                p("کن واتانابه", "Ken Watanabe")
            ),
            reviews = listOf(
                rev(
                    "آرش محبی", "Arash Mohabi",
                    "سفری به عمق ذهن؛ تلفیقی از هیجان و فلسفه",
                    "نولان با «تلقین» معمایی می‌سازد که تماشاگر را تا دقیقهٔ آخر درگیر می‌کند. لایه‌های موازی رویا و واقعیت، همراه با اجرای بی‌نقص بازیگران، اثری می‌سازد که هر بار دیدنش زوایای تازه‌ای دارد.",
                    9.0
                ),
                rev(
                    "نگار جلالی", "Negar Jalaali",
                    "چشم‌اندازی سینمایی که فراتر از سرگرمی است",
                    "جلوه‌های بصری و موسیقی هانس زیمر چنان با ساختار فیلم یکی شده‌اند که «تلقین» را به تجربه‌ای فراموش‌نشدنی تبدیل می‌کنند. پایان‌بندی چندپهلو، گفت‌وگوهای فراوانی را دربارهٔ ماهیت واقعیت برانگیخته است.",
                    8.5
                )
            ),
            comments = listOf(
                com("علی محمدی", "۲ روز پیش", "یکی از معدود فیلم‌هایی که هر بار دیدنش، یک فیلم جدید است.", 342),
                com("زهرا احمدی", "۱ هفته پیش", "ساختار لایه‌ای داستان فوق‌العاده است؛ حتماً با دقت ببینید.", 187),
                com("حسین رضایی", "۳ هفته پیش", "موسیقی پایان فیلم سال‌هاست در ذهنم مانده.", 96)
            )
        ),

        m(
            id = "mv-shawshank",
            titleFa = "رستگاری در شاوشنک",
            titleEn = "The Shawshank Redemption",
            year = 1994,
            rating = 9.3,
            votes = 2_900_000,
            runtime = 142,
            genres = g(MockGenres.DRAMA, MockGenres.CRIME),
            synopsis = "اندی دوفرین، بانکدار موفق، به جرم قتلی که مرتکب نشده به زندان شاوشنک فرستاده می‌شود. او در فضای خشن زندان با «رد» دوست می‌شود و در طول سال‌ها امید را به تاریک‌ترین گوشه‌های زندان می‌برد؛ امیدی که سرانجام راه رهایی را به او نشان می‌دهد.",
            director = p("فرانک دارابونت", "Frank Darabont"),
            cast = cast(
                p("تیم رابینز", "Tim Robbins"),
                p("مورگان فریمن", "Morgan Freeman"),
                p("باب گانتون", "Bob Gunton"),
                p("ویلیام سدلر", "William Sadler")
            ),
            reviews = listOf(
                rev(
                    "فرهاد نیکنام", "Farhad Niknam",
                    "قصهٔ امید در قاب زندان",
                    "«رستگاری در شاوشنک» دربارهٔ زندان نیست؛ دربارهٔ امیدی است که هیچ دیواری نمی‌تواند آن را در خود حبس کند. روایت آرام و عمیق دارابونت و بازی درخشان فریمن، این فیلم را به اثری جاودانه تبدیل کرده است.",
                    9.5
                )
            ),
            comments = listOf(
                com("مریم کاظمی", "۵ روز پیش", "هر بار که می‌بینمش، ته‌دلم خالی می‌شود؛ شاهکار.", 521),
                com("محمد صادقی", "۲ هفته پیش", "پایان‌بندی آن را فراموش نمی‌کنم.", 233)
            )
        ),

        m(
            id = "mv-godfather",
            titleFa = "پدرخوانده",
            titleEn = "The Godfather",
            year = 1972,
            rating = 9.2,
            votes = 2_000_000,
            runtime = 175,
            genres = g(MockGenres.CRIME, MockGenres.DRAMA),
            synopsis = "داستان خانوادهٔ قدرتمند «کورلئونه» و ویتو، پدرخواندهٔ سالخورده‌ای که امپراتوری جنایی خود را به دست فرزندش مایکل می‌سپارد؛ جوانی که به‌رغم میلش وارد دنیای تاریک خانواده می‌شود و دگرگونی‌اش آغاز می‌گردد.",
            director = p("فرانسیس فورد کوپولا", "Francis Ford Coppola"),
            cast = cast(
                p("مارلون براندو", "Marlon Brando"),
                p("آل پاچینو", "Al Pacino"),
                p("جیمز کان", "James Caan"),
                p("رابرت دووال", "Robert Duvall")
            ),
            reviews = listOf(
                rev(
                    "امیر کاویانی", "Amir Kavyani",
                    "حماسه‌ای که سینمای گنگستری را بازتعریف کرد",
                    "پدرخوانده فقط یک فیلم جنایی نیست؛ حماسه‌ای دربارهٔ قدرت، خانواده و تباهی است. کارگردانی کوپولا و چهرهٔ فراموش‌نشدنی براندو، آن را به یکی از مهم‌ترین آثار تاریخ سینما بدل کرده است.",
                    9.5
                )
            ),
            comments = listOf(
                com("فاطمه نوری", "۱ هفته پیش", "دیالوگ‌ها و فضاسازی این فیلم واقعاً بی‌نظیر است.", 264),
                com("رضا قاسمی", "۱ ماه پیش", "پیشنهاد غیرقابل ردّی که براندو داد را هیچ‌وقت فراموش نمی‌کنم!", 410)
            )
        ),

        m(
            id = "mv-darkknight",
            titleFa = "شوالیهٔ تاریکی",
            titleEn = "The Dark Knight",
            year = 2008,
            rating = 9.0,
            votes = 2_900_000,
            runtime = 152,
            genres = g(MockGenres.ACTION, MockGenres.CRIME, MockGenres.DRAMA),
            trending = true,
            synopsis = "بتمن با همکاری کمیسر گوردون و دادستان ناحیه، هاروی دنت، به جنگ جرم‌وخیزی گاتام می‌رود؛ اما ظهور جوکر، هرج‌ومرج‌گری که هیچ قانونی برایش معنایی ندارد، همهٔ باورهای ابرقهرمان را به چالش می‌کشد و گاتام را در آستانهٔ فروپاشی اخلاقی قرار می‌دهد.",
            director = p("کریستوفر نولان", "Christopher Nolan"),
            cast = cast(
                p("کریستین بیل", "Christian Bale"),
                p("هیث لجر", "Heath Ledger"),
                p("آرون اکهارت", "Aaron Eckhart"),
                p("مایکل کین", "Michael Caine"),
                p("گری اولدمن", "Gary Oldman")
            ),
            reviews = listOf(
                rev(
                    "سحر افشار", "Sahar Afshar",
                    "جوکرِ لجر؛ شخصیتی که از فیلم فراتر رفت",
                    "«شوالیهٔ تاریکی» مرز میان ابرقهرمانی و تریلر روان‌شناختی را درنوردیده است. بازی درخشان و آخرالزمانی هیث لجر، فیلم را به پدیده‌ای فرهنگی تبدیل کرد که سال‌ها بعد همچنان درباره‌اش صحبت می‌شود.",
                    9.0
                ),
                rev(
                    "پویا تهرانی", "Pouya Tehrani",
                    "آشوب و نظم؛ دو روی یک سکه",
                    "نولان با داستانی چندلایه و دیالوگ‌هایی عمیق نشان می‌دهد که مرز میان قهرمان و شرور چقدر می‌تواند باریک باشد. اکشن‌های نفس‌گیر در خدمت روایت‌اند، نه برعکس.",
                    8.5
                )
            ),
            comments = listOf(
                com("سارا موسوی", "۳ روز پیش", "بازی هیث لجر یک کلاس درس بازیگری است.", 388),
                com("امیرحسین کریمی", "۲ هفته پیش", "«چرا این‌قدر جدی؟» — بهترین سکانس تاریخ سینما!", 301)
            )
        ),

        m(
            id = "mv-interstellar",
            titleFa = "میان‌ستلاری",
            titleEn = "Interstellar",
            year = 2014,
            rating = 8.7,
            votes = 2_100_000,
            runtime = 169,
            genres = g(MockGenres.SCIFI, MockGenres.ADVENTURE, MockGenres.DRAMA),
            trending = true,
            synopsis = "زمین در حال نابودی است و کوپر، خلبانِ کشاورزِ پیشین، برای نجات بشریت مأموریتی میان‌ستاره‌ای را می‌پذیرد: عبور از یک کرم‌چاله برای یافتن خانه‌ای تازه برای انسان. اما دوری از فرزندانش و گذر زمان نسبی، هزینه‌ای است که او باید بپردازد.",
            director = p("کریستوفر نولان", "Christopher Nolan"),
            cast = cast(
                p("متیو مک‌کانهی", "Matthew McConaughey"),
                p("آن هت‌اوی", "Anne Hathaway"),
                p("جسیکا چستین", "Jessica Chastain"),
                p("مایکل کین", "Michael Caine")
            ),
            reviews = listOf(
                rev(
                    "مینا صادقی", "Mina Sadeghi",
                    "عشق به مثابه نیرویی فراتر از فیزیک",
                    "نولان علم و احساس را در هم می‌آمیزد تا داستانی دربارهٔ انسانیت و پیوند والدین و فرزند روایت کند. تصاویر سیاه‌چاله و کرم‌چاله از نظر علمی دقیق و از نظر بصری خیره‌کننده‌اند.",
                    8.5
                )
            ),
            comments = listOf(
                com("نگین عباسی", "۱ هفته پیش", "سکانس پیام‌ها از فضا هنوز هم اشکم را درمی‌آورد.", 276),
                com("پارسا قهرمانی", "۳ هفته پیش", "ترکیب موسیقی و فضا، تجربه‌ای سینمایی محض است.", 154)
            )
        ),

        m(
            id = "mv-pulpfiction",
            titleFa = "پالپ فیکشن",
            titleEn = "Pulp Fiction",
            year = 1994,
            rating = 8.9,
            votes = 2_200_000,
            runtime = 154,
            genres = g(MockGenres.CRIME, MockGenres.DRAMA),
            synopsis = "روایت‌های به‌هم‌پیچیدهٔ چند جنایتکار لس‌آنجلسی — دو قاتل قراردادی، بوکسوری که قرار است مبارزه را ببازد و همسر رئیس باند — در ساختاری غیرخطی روایت می‌شوند که در آن گفت‌وگوها همان‌قدر مهم‌اند که خشونت.",
            director = p("کوئنتین تارانتینو", "Quentin Tarantino"),
            cast = cast(
                p("جان تراولتا", "John Travolta"),
                p("ساموئل ال. جکسون", "Samuel L. Jackson"),
                p("اوما تورمن", "Uma Thurman"),
                p("بروس ویلیس", "Bruce Willis")
            ),
            reviews = listOf(
                rev(
                    "کیانوش رستمی", "Kianoush Rostami",
                    "سینمایی که با قواعد قدیمی بازی می‌کند",
                    "تارانتینو با «پالپ فیکشن» روایت را از نو چید و به فرهنگ عامه ادای دین کرد. دیالوگ‌های ماندگار و شخصیت‌های عجیبش، این فیلم را به سنگ‌بنای سینمای مستقل بدل کردند.",
                    8.5
                )
            ),
            comments = listOf(
                com("آیدا رحیمی", "۴ روز پیش", "ساختار غیرخطی داستان برای سال ۹۴ واقعاً جسورانه بود.", 209)
            )
        ),

        m(
            id = "mv-parasite",
            titleFa = "انگل",
            titleEn = "Parasite",
            year = 2019,
            rating = 8.5,
            votes = 950_000,
            runtime = 132,
            genres = g(MockGenres.DRAMA, MockGenres.THRILLER, MockGenres.COMEDY),
            trending = true,
            synopsis = "خانوادهٔ فقیر «کیم» به‌تدریج با ترفندهای مختلف وارد زندگی خانواده‌ای مرفه می‌شوند و هر کدام جایی در خانهٔ بزرگ آن‌ها پیدا می‌کنند. اما این مهمانی آرام، راز تاریکی را در زیرزمین خانه پنهان دارد که همه‌چیز را دگرگون می‌کند.",
            director = p("بونگ جون-هو", "Bong Joon-ho"),
            cast = cast(
                p("سونگ کانگ-هو", "Song Kang-ho"),
                p("لی سون-کیون", "Lee Sun-kyun"),
                p("چو یو-جونگ", "Cho Yeo-jeong"),
                p("چوی وو-شیک", "Choi Woo-shik")
            ),
            reviews = listOf(
                rev(
                    "ترانه عابدینی", "Taraneh Abedini",
                    "طنزی تلخ دربارهٔ شکاف طبقاتی",
                    "«انگل» در یک قاب، کمدی، دلهره و درام اجتماعی را کنار هم می‌نشاند و با چرخش‌های غیرمنتظره، تماشاگر را تا پایان شوکه نگه می‌دارد. بونگ جون-هو با این فیلم مرزهای ژانر را جابه‌جا کرد.",
                    9.0
                )
            ),
            comments = listOf(
                com("میلاد توکلی", "۲ روز پیش", "چرخش داستان در نیمهٔ دوم، آن را تبدیل به یک کلاس درس فیلم‌نامه‌نویسی کرد.", 318),
                com("شیدا ملکی", "۲ هفته پیش", "طنز تلخ و هوشمندانه دربارهٔ جامعه؛ استادانه.", 145)
            )
        ),

        m(
            id = "mv-oppenheimer",
            titleFa = "اوپن‌هایمر",
            titleEn = "Oppenheimer",
            year = 2023,
            rating = 8.3,
            votes = 800_000,
            runtime = 180,
            genres = g(MockGenres.DRAMA, MockGenres.HISTORY, MockGenres.BIOGRAPHY),
            synopsis = "داستان «جی. رابرت اوپن‌هایمر»، فیزیک‌دانی که پروژهٔ منهتن را برای ساخت نخستین بمب اتمی رهبری کرد و پس از آن، سال‌ها با وجدانِ آشفتهٔ خود و پیامدهای اخلاقی اختراعش دست‌وپنجه نرم کرد.",
            director = p("کریستوفر نولان", "Christopher Nolan"),
            cast = cast(
                p("کیلین مورفی", "Cillian Murphy"),
                p("امیلی بلانت", "Emily Blunt"),
                p("مت دیمون", "Matt Damon"),
                p("رابرت داونی جونیور", "Robert Downey Jr.")
            ),
            reviews = listOf(
                rev(
                    "الهام صدر", "Elham Sadr",
                    "پرتره‌ای از نابغه‌ای که دنیا را تغییر داد",
                    "نولان با تکیه بر اجرای بی‌نظیر مورفی، روایتی پرتنش از تاریخ ساخت بمب اتم ارائه می‌کند. گفت‌وگوهای حقوقی نیمهٔ دوم فیلم، دلهره‌ای به اندازهٔ هر اکشنی دارد.",
                    8.5
                )
            ),
            comments = listOf(
                com("کاوه ایرانی", "۱ هفته پیش", "نقش مورفی یک پدیده است؛ باید روی پردهٔ بزرگ دید.", 197),
                com("لیدا شریفی", "۱ ماه پیش", "پایان فیلم با آن موسیقی، خیلی سنگین و تکان‌دهنده بود.", 88)
            )
        ),

        m(
            id = "mv-dune2",
            titleFa = "تلماسه: بخش دوم",
            titleEn = "Dune: Part Two",
            year = 2024,
            rating = 8.5,
            votes = 500_000,
            runtime = 166,
            genres = g(MockGenres.SCIFI, MockGenres.ADVENTURE),
            trending = true,
            synopsis = "پل اتریدیز در میان قبایل فرمن به رهبری تبدیل می‌شود و در جست‌وجوی انتقام از خاندان هارکونن و نجات سیارهٔ تلماسه، با سرنوشتی روبرو می‌شود که او را به سوی یک جنگ مقدس تمام‌عیار می‌کشاند.",
            director = p("دنی ویلنوو", "Denis Villeneuve"),
            cast = cast(
                p("تیموتی شالامی", "Timothée Chalamet"),
                p("زندیا", "Zendaya"),
                p("ربکا فرگوسن", "Rebecca Ferguson"),
                p("خاویر باردم", "Javier Bardem")
            ),
            reviews = listOf(
                rev(
                    "آرش محبی", "Arash Mohabi",
                    "اپرای فضایی در مقیاسی خیره‌کننده",
                    "ویلنوو جهانی می‌سازد که در هر قابش عظمت و ظرافت دیده می‌شود. اقتباس وفادارانه از رمان هربرت همراه با تصاویر عظیم بیابانی، «تلماسه» را به یکی از بهترین حماسه‌های علمی-تخیلی سینما تبدیل کرده است.",
                    9.0
                )
            ),
            comments = listOf(
                com("بابک نادری", "۵ روز پیش", "صحنه‌های سوارشدن بر کرم‌های شنی را باید در سالن سینما تجربه کرد.", 176)
            )
        ),

        m(
            id = "mv-whiplash",
            titleFa = "ویپلش",
            titleEn = "Whiplash",
            year = 2014,
            rating = 8.5,
            votes = 1_000_000,
            runtime = 106,
            genres = g(MockGenres.DRAMA),
            synopsis = "اندرو نیمن، درامر جوان و جاه‌طلب، وارد گروه موسیقی «کنسرواتوار شافر» به رهبری استاد ترنس فلچر می‌شود؛ استادی که با روش‌های خشن و تحقیرآمیز خود، مرز میان کمال‌گرایی و دیوانگی را برای اندرو مبهم می‌کند.",
            director = p("دیمین شزل", "Damien Chazelle"),
            cast = cast(
                p("مایلز تلر", "Miles Teller"),
                p("جی. کی. سیمونز", "J. K. Simmons")
            ),
            reviews = listOf(
                rev(
                    "نگار جلالی", "Negar Jalaali",
                    "درام‌ای نفس‌گیر دربارهٔ بهای کمال",
                    "سیمونز نقشی می‌آفریند که همزمان نفرت‌انگیز و ستودنی است. صحنه‌های درام و پایان‌بندی خیره‌کنندهٔ فیلم، «ویپلش» را به اثری فراموش‌نشدنی تبدیل کرده است.",
                    8.5
                )
            ),
            comments = listOf(
                com("هومن صالحی", "۳ روز پیش", "سکانس پایانی را تا آخرین ثانیه با ضربان قلب بالا دیدم.", 233)
            )
        ),

        m(
            id = "mv-separation",
            titleFa = "جدایی نادر از سیمین",
            titleEn = "A Separation",
            year = 2011,
            rating = 8.3,
            votes = 280_000,
            runtime = 123,
            genres = g(MockGenres.DRAMA),
            synopsis = "نادر و سیمین برای مهاجرت تصمیم می‌گیرند اما اختلاف‌نظر آن‌ها به جدایی می‌انجامد. نادر برای نگهداری از پدر بیمارش مجبور است پرستاری استخدام کند؛ پرستاری که حضورش، زنجیره‌ای از درگیری‌ها، دروغ‌ها و داوری‌های اخلاقی را رقم می‌زند.",
            director = p("اصغر فرهادی", "Asghar Farhadi"),
            cast = cast(
                p("پیمان معادی", "Peyman Moadi"),
                p("لیلا حاتمی", "Leila Hatami"),
                p("سارینا فرهادی", "Sarina Farhadi"),
                p("شهاب حسینی", "Shahab Hosseini")
            ),
            reviews = listOf(
                rev(
                    "فرهاد نیکنام", "Farhad Niknam",
                    "سینمای اجتماعی ایران در اوج بلوغ",
                    "فرهادی با فیلمنامه‌ای چندلایه، اخلاقیات را در میانهٔ معماهای کوچک و بزرگ نشان می‌دهد. هر شخصیت «حق» دارد و همین، تراژدی واقعی داستان است.",
                    9.0
                )
            ),
            comments = listOf(
                com("نازنین کریمی", "۲ روز پیش", "افتخاری برای سینمای ایران؛ هر شخصیت درست‌فکر می‌کند و همه اشتباه می‌کنند.", 412),
                com("امید صحرایی", "۱ ماه پیش", "دیالوگ‌نویسی فرهادی در این فیلم در اوج است.", 168)
            )
        ),

        m(
            id = "mv-schindler",
            titleFa = "فهرست شیندلر",
            titleEn = "Schindler's List",
            year = 1993,
            rating = 9.0,
            votes = 1_400_000,
            runtime = 195,
            genres = g(MockGenres.DRAMA, MockGenres.HISTORY, MockGenres.WAR),
            synopsis = "اسکار شیندلر، تاجر آلمانی، در دوران هولوکاست کارخانه‌ای اداره می‌کند و با به‌کارگیری یهودیان، ابتدا از آن‌ها بهره‌کشی می‌کند؛ اما رفته‌رفته با دیدن وحشت‌های نازی‌ها، جان هزاران نفر را با قرار دادن نامشان در «فهرست شیندلر» نجات می‌دهد.",
            director = p("استیون اسپیلبرگ", "Steven Spielberg"),
            cast = cast(
                p("لیام نیسون", "Liam Neeson"),
                p("بن کینگزلی", "Ben Kingsley"),
                p("رالف فاینس", "Ralph Fiennes")
            ),
            reviews = listOf(
                rev(
                    "الهام صدر", "Elham Sadr",
                    "سینما در برابر تاریخ؛ اثری غیرقابل‌انکار",
                    "اسپیلبرگ بدون شعارزدگی، سیاه‌ترین صفحهٔ تاریخ را روایت می‌کند و در عین حال به انسانیت و امکان رستگاری باور دارد. سکانس‌های سیاه‌وسفید این فیلم در حافظهٔ جمعی سینما حک شده‌اند.",
                    9.5
                )
            ),
            comments = listOf(
                com("مهسا جعفری", "۱ هفته پیش", "تماشای آن سکانس‌ها سخت است اما لازم؛ فیلمی که باید دید.", 289)
            )
        ),

        m(
            id = "mv-forrest",
            titleFa = "فارست گامپ",
            titleEn = "Forrest Gump",
            year = 1994,
            rating = 8.8,
            votes = 2_200_000,
            runtime = 142,
            genres = g(MockGenres.DRAMA, MockGenres.ROMANCE),
            synopsis = "فارست گامپ، پسر ساده‌دلی که ضریب هوشی پایینی دارد اما قلبی پاک، در مسیر زندگی‌اش در مهم‌ترین رویدادهای نیم‌قرن آمریکا نقش می‌آفریند؛ در حالی که تنها آرزویش رسیدن به عشق دوران کودکی‌اش، جنی است.",
            director = p("رابرت زمکیس", "Robert Zemeckis"),
            cast = cast(
                p("تام هنکس", "Tom Hanks"),
                p("رابین رایت", "Robin Wright"),
                p("گری سینایس", "Gary Sinise"),
                p("سالی فیلد", "Sally Field")
            ),
            reviews = listOf(
                rev(
                    "امیر کاویانی", "Amir Kavyani",
                    "زندگی مثل یک جعبه شکلات است",
                    "«فارست گامپ» با ترکیب طنز، احساس و تاریخ، یکی از محبوب‌ترین درام‌های دههٔ نود است. هنکس در نقشی که به نظر ساده می‌رسد، عمقی مثال‌زدنی خلق کرده است.",
                    8.5
                )
            ),
            comments = listOf(
                com("سپیده عظیمی", "۴ روز پیش", "فیلمی که هم می‌خنداند و هم اشک‌تان را درمی‌آورد.", 221)
            )
        ),

        m(
            id = "mv-spiritedaway",
            titleFa = "شهر اشباح",
            titleEn = "Spirited Away",
            year = 2001,
            rating = 8.6,
            votes = 850_000,
            runtime = 125,
            genres = g(MockGenres.ANIMATION, MockGenres.FANTASY, MockGenres.FAMILY),
            synopsis = "چیهیرو، دختر ده ساله، هنگام نقل مکان به خانهٔ جدید وارد دنیای ارواح می‌شود و پدر و مادرش به خوک تبدیل می‌گردند. او برای نجات آن‌ها باید در حمام عمومی ارواح کار کند و نام واقعی‌اش را به خاطر بسپارد؛ نامی که کلید آزادی اوست.",
            director = p("هایائو میازاکی", "Hayao Miyazaki"),
            cast = cast(
                p("رومی هیراگی", "Rumi Hiiragi"),
                p("میو ایرینو", "Miyu Irino"),
                p("ماری ناتسوکی", "Mari Natsuki")
            ),
            reviews = listOf(
                rev(
                    "مینا صادقی", "Mina Sadeghi",
                    "رویایی که فقط انیمیشن می‌تواند بسازد",
                    "میازاکی جهانی خلق می‌کند پر از ارواح، جادو و موجودات شگفت‌انگیز که هم برای کودکان جذاب است و هم برای بزرگ‌سالان پر از لایه‌های معنایی. «شهر اشباح» پادشاه بی‌چون‌وچرای انیمه است.",
                    9.0
                )
            ),
            comments = listOf(
                com("آرش نیک‌بخت", "۲ هفته پیش", "طراحی دنیا و شخصیت‌ها چنان غنی است که هر فریمش یک تابلوست.", 174)
            )
        ),

        m(
            id = "mv-fightclub",
            titleFa = "باشگاه مبارزه",
            titleEn = "Fight Club",
            year = 1999,
            rating = 8.8,
            votes = 2_300_000,
            runtime = 139,
            genres = g(MockGenres.DRAMA, MockGenres.THRILLER),
            synopsis = "کارمندی بی‌نام که از زندگی مصرف‌گرایانه‌اش بیزار است، با «تایلر دوردن»، صابون‌سازِ هرج‌ومرج‌طلب آشنا می‌شود. آن دو «باشگاه مبارزه» را تأسیس می‌کنند؛ جایی برای تخلیهٔ خشم، که آرام‌آرام به جنبشی بزرگ و خطرناک بدل می‌شود.",
            director = p("دیوید فینچر", "David Fincher"),
            cast = cast(
                p("ادوارد نورتون", "Edward Norton"),
                p("برد پیت", "Brad Pitt"),
                p("هلنا بونهام کارتر", "Helena Bonham Carter")
            ),
            reviews = listOf(
                rev(
                    "پویا تهرانی", "Pouya Tehrani",
                    "فریادی خشمگین علیه دنیای مصرف",
                    "فینچر با نگاهی تاریک و طنزی گزنده، بحران هویت مرد مدرن را روایت می‌کند. چرخش پایانی فیلم، آن را به یکی از بحث‌برانگیزترین آثار دههٔ نود تبدیل کرده است.",
                    8.0
                )
            ),
            comments = listOf(
                com("کیان یوسفی", "۳ روز پیش", "قانون اول باشگاه مبارزه: دربارهٔ باشگاه مبارزه صحبت نکنید!", 356)
            )
        )
    )

    // ------------------------------------------------------------------ utils

    private fun m(
        id: String,
        titleFa: String,
        titleEn: String,
        year: Int,
        rating: Double,
        votes: Int,
        runtime: Int,
        genres: List<Genre>,
        trending: Boolean = false,
        synopsis: String,
        director: Person,
        cast: List<Person>,
        reviews: List<CriticReview> = emptyList(),
        comments: List<UserComment> = emptyList()
    ) = CatalogItem(
        id = id,
        type = ContentType.MOVIE,
        titleFa = titleFa,
        titleEn = titleEn,
        year = year,
        runtimeMinutes = runtime,
        genres = genres,
        imdbRating = rating,
        voteCount = votes,
        synopsis = synopsis,
        director = director,
        cast = cast,
        criticReviews = reviews,
        userComments = comments,
        isTrending = trending
    )

    private fun g(vararg genres: Genre): List<Genre> = genres.toList()

    private fun cast(vararg people: Person): List<Person> = people.toList()
}
